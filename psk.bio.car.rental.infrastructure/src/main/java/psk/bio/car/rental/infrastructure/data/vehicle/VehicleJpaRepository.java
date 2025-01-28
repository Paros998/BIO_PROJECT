package psk.bio.car.rental.infrastructure.data.vehicle;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import psk.bio.car.rental.application.vehicle.Vehicle;
import psk.bio.car.rental.application.vehicle.VehicleRepository;
import psk.bio.car.rental.application.vehicle.VehicleState;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
public interface VehicleJpaRepository extends JpaRepository<VehicleEntity, UUID>, VehicleRepository {

    List<VehicleEntity> findByModel(String model);

    Optional<VehicleEntity> findByPlate(String plate);

    Page<VehicleEntity> findByState(VehicleState state, Pageable pageable);

    Optional<VehicleEntity> findByIdAndState(@NonNull UUID id, @NonNull VehicleState state);

    List<VehicleEntity> findByState(@NonNull VehicleState state);

    // -------------------------------------------

    @Override
    default Optional<Vehicle> findVehicleByPlate(final @NonNull String plate) {
        return findByPlate(plate).map(Vehicle.class::cast);
    }

}
