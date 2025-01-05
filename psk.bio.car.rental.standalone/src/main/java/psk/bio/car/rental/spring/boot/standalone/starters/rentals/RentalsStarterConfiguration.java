package psk.bio.car.rental.spring.boot.standalone.starters.rentals;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import psk.bio.car.rental.infrastructure.data.payments.PaymentJpaRepository;
import psk.bio.car.rental.infrastructure.data.rentals.RentalJpaRepository;
import psk.bio.car.rental.infrastructure.data.user.UserJpaRepository;
import psk.bio.car.rental.infrastructure.data.vehicle.VehicleJpaRepository;

@Configuration
@ConditionalOnProperty(prefix = "car-rental.rentals.starter", value = "enabled", havingValue = "true")
public class RentalsStarterConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "car-rental.rentals.starter.data")
    public RentalsToAddConfig rentalsToAddConfig() {
        return new RentalsToAddConfig();
    }

    @Bean
    public RentalsInitializer rentalsInitializer(
            final @Lazy RentalJpaRepository rentalJpaRepository,
            final @Lazy VehicleJpaRepository vehicleJpaRepository,
            final @Lazy UserJpaRepository userJpaRepository,
            final @Lazy PaymentJpaRepository paymentJpaRepository
    ) {
        return new RentalsInitializer(
                rentalJpaRepository,
                vehicleJpaRepository,
                userJpaRepository,
                paymentJpaRepository,
                rentalsToAddConfig());
    }
}
