package psk.bio.car.rental.infrastructure.data.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import psk.bio.car.rental.application.payments.CompanyFinancialConfiguration;
import psk.bio.car.rental.application.payments.PaymentStatus;
import psk.bio.car.rental.application.payments.PaymentType;
import psk.bio.car.rental.application.rental.Rental;
import psk.bio.car.rental.application.rental.RentalService;
import psk.bio.car.rental.application.rental.RentalState;
import psk.bio.car.rental.application.security.UserContextValidator;
import psk.bio.car.rental.infrastructure.data.client.ClientEntity;
import psk.bio.car.rental.infrastructure.data.employee.EmployeeEntity;
import psk.bio.car.rental.infrastructure.data.payments.PaymentEntity;
import psk.bio.car.rental.infrastructure.data.rentals.RentalEntity;
import psk.bio.car.rental.infrastructure.data.rentals.RentalJpaRepository;
import psk.bio.car.rental.infrastructure.data.vehicle.VehicleEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired, @Lazy}))
public class RentalServiceImpl implements RentalService {
    private static final Integer DEFAULT_PAYMENT_DAYS_DUE = 7;

    private final RentalJpaRepository rentalRepository;
    private final UserContextValidator userContextValidator;

    private final CompanyFinancialConfiguration financialConfiguration;

    private final ClientServiceImpl clientService;
    private final PaymentServiceImpl paymentService;
    private final VehicleServiceImpl vehicleService;

    @Override
    @Transactional
    public UUID rentVehicle(final @NonNull UUID vehicleId, final @NonNull UUID clientId, final @NonNull Integer numberOfDays) {
        if (numberOfDays <= 0) {
            throw new IllegalArgumentException("Number of days must be greater than 0");
        }
        userContextValidator.checkUserPerformingAction(clientId);
        final VehicleEntity vehicle = vehicleService.findReadyToRentVehicle(vehicleId);

        final ClientEntity client = clientService.findById(clientId);
        final RentalEntity rentalEntity = RentalEntity.builder()
                .state(RentalState.NEW)
                .vehicle(vehicle)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(numberOfDays))
                .client(client)
                .build();

        final PaymentEntity payment = PaymentEntity.builder()
                .associatedVehicle(vehicle)
                .associatedRental(rentalEntity)
                .type(PaymentType.RENTAL_FEE)
                .status(PaymentStatus.PENDING)
                .creationDate(LocalDateTime.now())
                .chargedClient(client)
                .dueDate(LocalDate.now().plusDays(DEFAULT_PAYMENT_DAYS_DUE))
                .accountNumber(financialConfiguration.getCompanyBankAccountNumber())
                .amount((vehicle).getRentPerDayPrice().multiply(new BigDecimal(numberOfDays)))
                .build();

        paymentService.save(payment);

        final VehicleEntity rentedVehicle = vehicle.rentVehicle(rentalEntity);
        vehicleService.save(rentedVehicle);

        return rentalRepository.save(rentalEntity).getId();
    }

    @Transactional
    public RentalEntity finishRental(final @NonNull UUID rentalId, final @NonNull EmployeeEntity employee) {
        final RentalEntity rental = rentalRepository.findById(rentalId)
                .filter(rentalEntity -> rentalEntity.getState().equals(RentalState.ACTIVE))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        rental.finishRental(employee);
        rentalRepository.save(rental);
        return rental;
    }

    @Override
    public RentalEntity getRental(@NonNull final UUID rentalId) {
        return rentalRepository.findById(rentalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public RentalEntity saveRental(@NonNull final Rental rental) {
        return rentalRepository.save((RentalEntity) rental);
    }

    public List<RentalEntity> findByClientId(final @NonNull UUID clientId) {
        return rentalRepository.findByClient(clientId.toString()).stream()
                .map(RentalEntity.class::cast)
                .toList();
    }
}
