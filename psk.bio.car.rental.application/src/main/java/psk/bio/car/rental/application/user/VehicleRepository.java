package psk.bio.car.rental.application.user;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface VehicleRepository {
  Collection<UserProjection> findAllVehicles();

  Optional<UserProjection> findById(UUID id);
}
