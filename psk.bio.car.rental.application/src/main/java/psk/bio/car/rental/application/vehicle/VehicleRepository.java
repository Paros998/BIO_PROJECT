package psk.bio.car.rental.application.vehicle;

import java.util.Optional;

public interface VehicleRepository {

    Optional<Vehicle> findVehicleByPlate(String plate);
}
