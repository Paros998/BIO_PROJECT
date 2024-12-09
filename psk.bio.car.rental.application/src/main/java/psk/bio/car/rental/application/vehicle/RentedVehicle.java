package psk.bio.car.rental.application.vehicle;

import psk.bio.car.rental.application.rental.RentalProjection;

public interface RentedVehicle extends Vehicle {
    ReturnedVehicle returnVehicle();

    RentalProjection getCurrentRental();
}
