package psk.bio.car.rental.spring.boot.standalone.starters.vehicles;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import psk.bio.car.rental.infrastructure.data.vehicle.VehicleJpaRepository;

@Configuration
@ConditionalOnProperty(prefix = "car-rental.vehicles.starter", value = "enabled", havingValue = "true")
public class VehiclesStarterConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "car-rental.vehicles.starter.data")
    public VehiclesToAddConfig vehiclesToAddConfig() {
        return new VehiclesToAddConfig();
    }

    @Bean
    public VehiclesInitializer vehiclesInitializer(final @Lazy VehicleJpaRepository vehicleRepository) {
        return new VehiclesInitializer(vehicleRepository, vehiclesToAddConfig());
    }
}
