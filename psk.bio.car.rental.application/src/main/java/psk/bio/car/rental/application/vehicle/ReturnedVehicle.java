package psk.bio.car.rental.application.vehicle;

public interface ReturnedVehicle extends Vehicle {
    InRepairVehicle sendToRepairVehicle();

    ReadyToRentVehicle makeAvailableToRentVehicle();
}
