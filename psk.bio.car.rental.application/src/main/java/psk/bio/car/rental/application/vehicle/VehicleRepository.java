package psk.bio.car.rental.application.vehicle;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository {
    Vehicle save(NewVehicle vehicle);

    Vehicle save(InRepairVehicle vehicle);

    Vehicle save(ReadyToRentVehicle vehicle);

    Vehicle save(RentedVehicle vehicle);

    Vehicle save(ReturnedVehicle vehicle);

    // -----------------------------------

    Optional<Vehicle> findVehicleByPlate(String plate);

    List<Vehicle> findAllVehicles();
//
//    Optional<RentedVehicle> findById(UUID id);
//
//    Optional<RentedVehicle> findByModel(String model);
//
//    Optional<RentedVehicle> findByYear(int year);
//
//    Optional<RentedVehicle> findByModelAndYear(String model, int year);
}
