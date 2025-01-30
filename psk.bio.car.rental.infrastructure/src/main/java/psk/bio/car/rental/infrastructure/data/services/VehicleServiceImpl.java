package psk.bio.car.rental.infrastructure.data.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import psk.bio.car.rental.api.common.paging.PageRequest;
import psk.bio.car.rental.api.common.paging.PageResponse;
import psk.bio.car.rental.api.vehicles.AddVehicleRequest;
import psk.bio.car.rental.api.vehicles.VehicleModel;
import psk.bio.car.rental.application.payments.PaymentStatus;
import psk.bio.car.rental.application.payments.PaymentType;
import psk.bio.car.rental.application.security.ContextProvider;
import psk.bio.car.rental.application.security.exceptions.BusinessExceptionFactory;
import psk.bio.car.rental.application.user.UserProjection;
import psk.bio.car.rental.application.vehicle.VehicleService;
import psk.bio.car.rental.application.vehicle.VehicleState;
import psk.bio.car.rental.infrastructure.data.client.ClientEntity;
import psk.bio.car.rental.infrastructure.data.common.paging.PageMapper;
import psk.bio.car.rental.infrastructure.data.common.paging.SpringPageRequest;
import psk.bio.car.rental.infrastructure.data.common.paging.SpringPageResponse;
import psk.bio.car.rental.infrastructure.data.employee.EmployeeEntity;
import psk.bio.car.rental.infrastructure.data.mappers.VehicleMapper;
import psk.bio.car.rental.infrastructure.data.payments.PaymentEntity;
import psk.bio.car.rental.infrastructure.data.rentals.RentalEntity;
import psk.bio.car.rental.infrastructure.data.vehicle.VehicleEntity;
import psk.bio.car.rental.infrastructure.data.vehicle.VehicleJpaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static psk.bio.car.rental.application.security.exceptions.BusinessExceptionCodes.VEHICLE_IS_NOT_RENTED;
import static psk.bio.car.rental.application.security.exceptions.BusinessExceptionCodes.VEHICLE_WITH_SAME_PLATE_ALREADY_EXISTS;

@Log4j2
@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {
    private final VehicleJpaRepository vehicleRepository;
    private final ContextProvider contextProvider;

    @Autowired
    private @Lazy PaymentServiceImpl paymentService;

    public PageResponse<VehicleModel> searchVehicles(final VehicleState vehicleState, final PageRequest pageRequest) {
        SpringPageRequest springPageRequest = PageMapper.toSpringPageRequest(pageRequest);
        SpringPageResponse<VehicleEntity> vehicles;

        if (vehicleState != null) {
            vehicles = new SpringPageResponse<>(
                    vehicleRepository.findByState(vehicleState, springPageRequest.getRequest(VehicleEntity.class))
            );
        } else {
            vehicles = new SpringPageResponse<>(
                    vehicleRepository.findAll(springPageRequest.getRequest(VehicleEntity.class))
            );
        }

        final UserProjection currentUser = contextProvider.getCurrentUser();

        return PageMapper.toPageResponse(vehicles, vehicle ->  VehicleMapper.toVehicleModel(vehicle, currentUser.getRole()));
    }

    @Override
    @Transactional
    public UUID registerNewVehicle(final @NonNull AddVehicleRequest request) {
        if (vehicleRepository.findVehicleByPlate(request.getPlate()).isPresent()) {
            throw BusinessExceptionFactory.instantiateBusinessException(VEHICLE_WITH_SAME_PLATE_ALREADY_EXISTS);
        }

        final var vehicle = VehicleEntity.builder()
                .state(VehicleState.NEW)
                .plate(request.getPlate())
                .color(request.getColor())
                .model(request.getModel())
                .rentPerDayPrice(request.getRentPerDayPrice())
                .yearOfProduction(Year.of(request.getYearOfProduction()))
                .build();

        return vehicleRepository.save(vehicle).getVehicleId();
    }

    @Override
    public @NonNull VehicleEntity findReadyToRentVehicle(final @NonNull UUID vehicleId) {
        return vehicleRepository.findByIdAndState(vehicleId, VehicleState.READY_TO_RENT)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public VehicleEntity getVehicle(final @NonNull UUID vehicleId, final @NonNull VehicleState state) {
        return vehicleRepository.findByIdAndState(vehicleId, state).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Vehicle with id:[%s] and state:[%s] not found", vehicleId, state))
        );
    }

    public VehicleEntity getVehicle(final @NonNull UUID vehicleId, final @NonNull Set<VehicleState> states) {
        return vehicleRepository.findByIdAndStateIn(vehicleId, states).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Vehicle with id:[%s] in states:[%s] not found", vehicleId, states))
        );
    }

    @Transactional
    public void finishRepairsAndChargeCompany(final @NonNull VehicleEntity vehicle,
                                              final @NonNull EmployeeEntity createdBy,
                                              final @NonNull BigDecimal totalCost,
                                              final @NonNull String bankAccountNumber,
                                              final @NonNull LocalDate dueDate) {
        vehicle.finishRepairsAndMakeReadyToRent();
        vehicleRepository.save(vehicle);
        var payment = PaymentEntity.builder()
                .status(PaymentStatus.PENDING)
                .type(PaymentType.REPAIR)
                .accountNumber(bankAccountNumber)
                .amount(totalCost)
                .creationDate(LocalDateTime.now())
                .dueDate(dueDate)
                .associatedVehicle(vehicle)
                .associatedRental(null)
                .createdByEmployee(createdBy)
                .build();
        paymentService.save(payment);
        checkForInsuranceEnding(vehicle);
    }

    @Transactional
    public void finishRepairsAndChargeCustomer(final @NonNull VehicleEntity vehicle,
                                               final @NonNull EmployeeEntity createdBy,
                                               final @NonNull BigDecimal totalCost,
                                               final @NonNull String bankAccountNumber,
                                               final @NonNull LocalDate dueDate) {
        final RentalEntity rentalAfterRepairsWhereNeeded = vehicle.getLastRental();
        final ClientEntity chargedClient = Optional.ofNullable(rentalAfterRepairsWhereNeeded)
                .map(RentalEntity::getClient)
                .orElse(null);
        vehicle.finishRepairsAndMakeReadyToRent();
        vehicleRepository.save(vehicle);
        var payment = PaymentEntity.builder()
                .status(PaymentStatus.PENDING)
                .type(PaymentType.CLIENT_REPAIR)
                .accountNumber(bankAccountNumber)
                .amount(totalCost)
                .creationDate(LocalDateTime.now())
                .dueDate(dueDate)
                .associatedVehicle(vehicle)
                .chargedClient(chargedClient)
                .associatedRental(rentalAfterRepairsWhereNeeded)
                .createdByEmployee(createdBy)
                .build();
        paymentService.save(payment);
        checkForInsuranceEnding(vehicle);
    }

    @Transactional
    public void sendToRepair(final @NonNull VehicleEntity vehicle) {
        vehicle.sendToRepairVehicle();
        vehicleRepository.save(vehicle);
    }

    @Transactional
    public void makeVehicleReadyToRent(final @NonNull VehicleEntity vehicle) {
        vehicle.makeAvailableToRentVehicle();
        vehicleRepository.save(vehicle);
        checkForInsuranceEnding(vehicle);
    }

    @Transactional
    public void makeVehicleInsured(final @NonNull VehicleEntity vehicle,
                                   final @NonNull EmployeeEntity createdBy,
                                   final @NonNull String insuranceId,
                                   final @NonNull String bankAccountNumber,
                                   final @NonNull BigDecimal insuranceCost,
                                   final @NonNull LocalDate dueDate) {
        vehicle.insureVehicle(insuranceId, dueDate);
        vehicleRepository.save(vehicle);
        var payment = PaymentEntity.builder()
                .status(PaymentStatus.PENDING)
                .type(PaymentType.INSURANCE)
                .accountNumber(bankAccountNumber)
                .amount(insuranceCost)
                .creationDate(LocalDateTime.now())
                .dueDate(dueDate)
                .associatedVehicle(vehicle)
                .chargedClient(null)
                .associatedRental(null)
                .createdByEmployee(createdBy)
                .build();
        paymentService.save(payment);
        checkForInsuranceEnding(vehicle);
    }

    @Transactional
    public VehicleEntity returnVehicle(final @NonNull RentalEntity rental) {
        final VehicleEntity vehicle = rental.getVehicle();
        if (!vehicle.getState().equals(VehicleState.RENTED)) {
            throw BusinessExceptionFactory.instantiateBusinessException(VEHICLE_IS_NOT_RENTED);
        }
        vehicle.returnVehicle(rental);
        return vehicleRepository.save(vehicle);
    }

    @Transactional
    public void save(final @NonNull VehicleEntity vehicle) {
        vehicleRepository.save(vehicle);
    }

    @SuppressWarnings("checkstyle:MagicNumber")
    @Scheduled(timeUnit = TimeUnit.HOURS, fixedRate = 8L)
    @Transactional
    public void checkForInsuranceEnding() {
        log.info("Checking for insurance ending of vehicles.");
        validateVehiclesInsurance(vehicleRepository.findByState(VehicleState.READY_TO_RENT));
    }

    private void checkForInsuranceEnding(final @NonNull VehicleEntity vehicle) {
        validateVehiclesInsurance(Set.of(vehicle));
    }

    private void validateVehiclesInsurance(final @NonNull Collection<VehicleEntity> vehicles) {
        final List<VehicleEntity> vehiclesToRevokeInsurance = vehicles.stream()
                .filter(vehicle -> vehicle.getState() == VehicleState.READY_TO_RENT)
                .filter(vehicleEntity -> vehicleEntity.getEnsuredDueDate().isBefore(LocalDate.now())
                        || vehicleEntity.getEnsuredDueDate().isEqual(LocalDate.now()))
                .toList();

        log.info("Revoking vehicles insurances :{}", vehiclesToRevokeInsurance);
        vehiclesToRevokeInsurance.forEach(vehicleEntity -> {
            vehicleEntity.insuranceRevoked();
            vehicleRepository.save(vehicleEntity);
        });
        log.info("Revoking vehicles insurances finished, total vehicles revoked:{}", vehiclesToRevokeInsurance.size());
    }

}
