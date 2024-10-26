package psk.bio.car.rental.infrastructure.data.vehicle;

public interface VehicleJpaRepository {

  String findByModel(String model);
  String findByType(String type);
}
