package psk.bio.car.rental.application.vehicle;

import psk.bio.car.rental.application.rental.Rental;

public interface ReadyToRentVehicle extends Vehicle {
    RentedVehicle rentVehicle(Rental rental);
}
