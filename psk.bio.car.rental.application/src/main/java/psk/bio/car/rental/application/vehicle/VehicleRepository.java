package psk.bio.car.rental.application.vehicle;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface VehicleRepository {
  Collection<VehicleProjection> findAllVehicles();

  Optional<VehicleProjection> findById(UUID id);
  Optional<VehicleProjection> findByModel(String model);
  Optional<VehicleProjection> findByYear(int year);
  Optional<VehicleProjection> findByModelAndYear(String model, int year);
}
