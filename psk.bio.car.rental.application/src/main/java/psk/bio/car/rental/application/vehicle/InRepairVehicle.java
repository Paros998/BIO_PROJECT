package psk.bio.car.rental.application.vehicle;

public interface InRepairVehicle extends Vehicle {
    ReadyToRentVehicle finishRepairsAndMakeReadyToRent();
}
