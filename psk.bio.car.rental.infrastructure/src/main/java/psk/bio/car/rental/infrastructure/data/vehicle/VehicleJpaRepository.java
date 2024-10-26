package psk.bio.car.rental.infrastructure.data.vehicle;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleJpaRepository extends JpaRepository<VehicleEntity, UUID> {

  List<String> findByModel(String model);
  List<String> findByType(String type);
}
