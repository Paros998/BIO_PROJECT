package psk.bio.car.rental.application.vehicle;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface VehicleRepository {
    Collection<RentedVehicleProjection> findAllVehicles();

    Optional<RentedVehicleProjection> findById(UUID id);

    Optional<RentedVehicleProjection> findByModel(String model);

    Optional<RentedVehicleProjection> findByYear(int year);

    Optional<RentedVehicleProjection> findByModelAndYear(String model, int year);
}
