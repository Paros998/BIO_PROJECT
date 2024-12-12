package psk.bio.car.rental.infrastructure.data.vehicle;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import psk.bio.car.rental.application.vehicle.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
public interface VehicleJpaRepository extends JpaRepository<VehicleEntity, UUID>, VehicleRepository {

    List<VehicleEntity> findByModel(String model);

    Optional<VehicleEntity> findByPlate(String plate);

    @Override
    default Optional<Vehicle> findVehicleByPlate(final @NonNull String plate) {
        return findByPlate(plate).map(Vehicle.class::cast);
    }

    @Override
    default List<Vehicle> findAllVehicles() {
        return findAll().stream()
                .map(Vehicle.class::cast)
                .toList();
    }

    // -------------------------------------------


}
