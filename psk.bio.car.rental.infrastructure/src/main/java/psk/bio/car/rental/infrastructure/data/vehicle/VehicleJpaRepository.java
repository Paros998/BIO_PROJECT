package psk.bio.car.rental.infrastructure.data.vehicle;

import org.springframework.data.jpa.repository.JpaRepository;
import psk.bio.car.rental.application.vehicle.VehicleRepository;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
public interface VehicleJpaRepository extends JpaRepository<VehicleEntity, UUID>, VehicleRepository {

    List<VehicleEntity> findByModel(String model);

}
