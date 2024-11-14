package psk.bio.car.rental.application.vehicle;

public interface ReadyToRentVehicleProjection {
    RentedVehicleProjection rentVehicle(String rentalId);
}
