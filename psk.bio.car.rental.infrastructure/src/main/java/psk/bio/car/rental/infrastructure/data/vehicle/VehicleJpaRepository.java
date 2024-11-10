package psk.bio.car.rental.infrastructure.data.vehicle;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
public interface VehicleJpaRepository extends JpaRepository<VehicleEntity, UUID> {

    List<VehicleEntity> findByModel(String model);

}
