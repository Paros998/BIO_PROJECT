package psk.bio.car.rental.spring.boot.standalone.starters.vehicles;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.transaction.annotation.Transactional;
import psk.bio.car.rental.infrastructure.data.vehicle.VehicleJpaRepository;

@Log4j2
@RequiredArgsConstructor
public class VehiclesInitializer implements ApplicationRunner {
    private final VehicleJpaRepository vehicleRepository;
    private final VehiclesToAddConfig config;

    @Override
    @Transactional
    public void run(final ApplicationArguments args) {
        log.info("{} running", this.getClass().getSimpleName());

        final var vehiclesToAdd = config.getVehicles().stream()
                .filter(vehicle -> vehicleRepository.findVehicleByPlate(vehicle.getPlate()).isEmpty())
                .toList();

        vehicleRepository.saveAll(vehiclesToAdd);
        log.info("Initialized vehicles: {}", vehiclesToAdd);
    }
}
