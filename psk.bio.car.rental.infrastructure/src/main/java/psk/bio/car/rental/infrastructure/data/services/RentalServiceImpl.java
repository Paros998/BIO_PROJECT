package psk.bio.car.rental.infrastructure.data.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import psk.bio.car.rental.application.payments.PaymentStatus;
import psk.bio.car.rental.application.payments.PaymentType;
import psk.bio.car.rental.application.rental.Rental;
import psk.bio.car.rental.application.rental.RentalService;
import psk.bio.car.rental.application.rental.RentalState;
import psk.bio.car.rental.application.security.UserContextValidator;
import psk.bio.car.rental.application.user.UserRepository;
import psk.bio.car.rental.application.vehicle.ReadyToRentVehicle;
import psk.bio.car.rental.application.vehicle.RentedVehicle;
import psk.bio.car.rental.application.vehicle.VehicleRepository;
import psk.bio.car.rental.application.vehicle.VehicleService;
import psk.bio.car.rental.infrastructure.data.client.ClientEntity;
import psk.bio.car.rental.infrastructure.data.payments.PaymentEntity;
import psk.bio.car.rental.infrastructure.data.payments.PaymentJpaRepository;
import psk.bio.car.rental.infrastructure.data.rentals.RentalEntity;
import psk.bio.car.rental.infrastructure.data.rentals.RentalJpaRepository;
import psk.bio.car.rental.infrastructure.data.vehicle.VehicleEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {
    private static final Integer DEFAULT_PAYMENT_DAYS_DUE = 7;

    private final RentalJpaRepository rentalRepository;
    private final VehicleRepository vehicleRepository;
    private final PaymentJpaRepository paymentRepository;
    private final UserRepository userRepository;
    private final VehicleService vehicleService;
    private final UserContextValidator userContextValidator;

    @Override
    @Transactional
    public UUID rentVehicle(final @NonNull UUID vehicleId, final @NonNull UUID clientId, final @NonNull Integer numberOfDays) {
        userContextValidator.checkUserPerformingAction(clientId);
        final ReadyToRentVehicle vehicle = vehicleService.findReadyToRentVehicle(vehicleId);

        final ClientEntity client = (ClientEntity) userRepository.findById(String.valueOf(clientId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        final RentalEntity rentalEntity = RentalEntity.builder()
                .state(RentalState.NEW)
                .vehicle((VehicleEntity) vehicle)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(numberOfDays))
                .client(client)
                .build();

        final PaymentEntity payment = PaymentEntity.builder()
                .associatedVehicle((VehicleEntity) vehicle)
                .type(PaymentType.RENTAL_FEE)
                .status(PaymentStatus.PENDING)
                .creationDate(LocalDateTime.now())
                .chargedClient(client)
                .dueDate(LocalDate.now().plusDays(DEFAULT_PAYMENT_DAYS_DUE))
                .amount(((VehicleEntity) vehicle).getRentPerDayPrice().multiply(new BigDecimal(numberOfDays)))
                .build();

        paymentRepository.save(payment);

        final RentedVehicle rentedVehicle = vehicle.rentVehicle(rentalEntity);
        vehicleRepository.save(rentedVehicle);

        return rentalRepository.save(rentalEntity).getId();
    }

    @Override
    public Rental getRental(@NonNull final UUID rentalId) {
        return rentalRepository.findById(rentalId.toString())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public Rental saveRental(@NonNull final Rental rental) {
        return rentalRepository.save((RentalEntity) rental);
    }
}
