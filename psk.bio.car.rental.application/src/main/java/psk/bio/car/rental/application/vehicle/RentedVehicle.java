package psk.bio.car.rental.application.vehicle;

import psk.bio.car.rental.application.rental.Rental;

public interface RentedVehicle extends Vehicle {
    ReturnedVehicle returnVehicle();

    Rental getCurrentRental();
}
