package psk.bio.car.rental.application.vehicle;

public interface VehicleRepository {
    Vehicle save(NewVehicle vehicle);

    Vehicle save(InRepairVehicle vehicle);

    Vehicle save(ReadyToRentVehicle vehicle);

    Vehicle save(RentedVehicle vehicle);

    Vehicle save(ReturnedVehicle vehicle);

    // TODO rework rest
//    Collection<RentedVehicle> findAllVehicles();
//
//    Optional<RentedVehicle> findById(UUID id);
//
//    Optional<RentedVehicle> findByModel(String model);
//
//    Optional<RentedVehicle> findByYear(int year);
//
//    Optional<RentedVehicle> findByModelAndYear(String model, int year);
}
