package psk.bio.car.rental.spring.boot.standalone.starters.rentals;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;
import psk.bio.car.rental.application.user.UserRepository;
import psk.bio.car.rental.infrastructure.data.client.ClientEntity;
import psk.bio.car.rental.infrastructure.data.employee.EmployeeEntity;
import psk.bio.car.rental.infrastructure.data.payments.PaymentJpaRepository;
import psk.bio.car.rental.infrastructure.data.rentals.RentalEntity;
import psk.bio.car.rental.infrastructure.data.rentals.RentalJpaRepository;
import psk.bio.car.rental.infrastructure.data.vehicle.VehicleEntity;
import psk.bio.car.rental.infrastructure.data.vehicle.VehicleJpaRepository;

@SuppressWarnings("checkstyle:MagicNumber")
@Log4j2
@RequiredArgsConstructor
@Order(102)
public class RentalsInitializer implements ApplicationRunner {
    private final RentalJpaRepository rentalJpaRepository;
    private final VehicleJpaRepository vehicleJpaRepository;
    private final UserRepository userRepository;
    private final PaymentJpaRepository paymentJpaRepository;
    private final RentalsToAddConfig rentalsToAddConfig;

    @Override
    @Transactional
    public void run(final ApplicationArguments args) {
        log.info("{} running", this.getClass().getSimpleName());

        rentalsToAddConfig.getRentals().forEach(this::addRental);
    }

    private void addRental(final RentalsToAddConfig.RentalToAdd rental) {
        log.info("Initializing Rental: {}", rental);
        final ClientEntity client = (ClientEntity) userRepository.findByUsername(rental.getClientEmail()).orElseThrow();
        final EmployeeEntity employee = (EmployeeEntity) userRepository.findByUsername(rental.getEmployeeEmail()).orElseThrow();
        final VehicleEntity vehicle = vehicleJpaRepository.findByPlate(rental.getPlate()).orElseThrow();

        final RentalEntity rentalEntity = rental.getRentalEntity();

        rentalEntity.setClient((ClientEntity) client);
        rentalEntity.setParticipatingEmployee((EmployeeEntity) employee);
        rentalEntity.setVehicle(vehicle);

        if (!rentalEntity.getAssociatedPayments().isEmpty()) {
            rentalEntity.getAssociatedPayments().forEach(payment -> {
                payment.setChargedClient((ClientEntity) client);
                payment.setAssociatedVehicle(vehicle);
                payment.setCreatedByEmployee((EmployeeEntity) employee);
            });
        }

        paymentJpaRepository.saveAll(rentalEntity.getAssociatedPayments());
        rentalJpaRepository.save(rentalEntity);
        log.info("Rental added: {}", rentalEntity);
    }
}
