package psk.bio.car.rental.infrastructure.data.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import psk.bio.car.rental.api.common.paging.PageRequest;
import psk.bio.car.rental.api.common.paging.PageResponse;
import psk.bio.car.rental.api.vehicles.AddVehicleRequest;
import psk.bio.car.rental.api.vehicles.VehicleModel;
import psk.bio.car.rental.application.security.exceptions.BusinessExceptionFactory;
import psk.bio.car.rental.application.vehicle.VehicleService;
import psk.bio.car.rental.application.vehicle.VehicleState;
import psk.bio.car.rental.infrastructure.data.common.paging.PageMapper;
import psk.bio.car.rental.infrastructure.data.common.paging.SpringPageRequest;
import psk.bio.car.rental.infrastructure.data.common.paging.SpringPageResponse;
import psk.bio.car.rental.infrastructure.data.mappers.VehicleMapper;
import psk.bio.car.rental.infrastructure.data.rentals.RentalEntity;
import psk.bio.car.rental.infrastructure.data.vehicle.VehicleEntity;
import psk.bio.car.rental.infrastructure.data.vehicle.VehicleJpaRepository;

import java.time.LocalDate;
import java.time.Year;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static psk.bio.car.rental.application.security.exceptions.BusinessExceptionCodes.VEHICLE_WITH_SAME_PLATE_ALREADY_EXISTS;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {
    private final VehicleJpaRepository vehicleRepository;

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

        return PageMapper.toPageResponse(vehicles, VehicleMapper::toVehicleModel);
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
    public VehicleEntity returnVehicle(final @NonNull RentalEntity rental) {
        final VehicleEntity vehicle = rental.getVehicle();
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
        validateVehiclesInsurance(vehicleRepository.findByState(VehicleState.READY_TO_RENT));
    }

    private void checkForInsuranceEnding(final @NonNull VehicleEntity vehicle) {
        validateVehiclesInsurance(Set.of(vehicle));
    }

    private void validateVehiclesInsurance(final @NonNull Collection<VehicleEntity> vehicles) {
        vehicles.stream()
                .filter(vehicle -> vehicle.getState() == VehicleState.READY_TO_RENT)
                .filter(vehicleEntity -> vehicleEntity.getEnsuredDueDate().isBefore(LocalDate.now())
                        || vehicleEntity.getEnsuredDueDate().isEqual(LocalDate.now()))
                .forEach(vehicleEntity -> {
                    vehicleEntity.insuranceRevoked();
                    vehicleRepository.save(vehicleEntity);
                });
    }

}
