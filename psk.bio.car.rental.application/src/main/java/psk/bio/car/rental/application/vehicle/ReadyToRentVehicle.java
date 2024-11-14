package psk.bio.car.rental.application.vehicle;

public interface ReadyToRentVehicle extends Vehicle {
    RentedVehicle rentVehicle(String rentalId);
}
