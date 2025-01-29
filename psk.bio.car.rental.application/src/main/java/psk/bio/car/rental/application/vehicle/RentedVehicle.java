package psk.bio.car.rental.application.vehicle;

import lombok.NonNull;
import psk.bio.car.rental.application.rental.Rental;

public interface RentedVehicle extends Vehicle {
    ReturnedVehicle returnVehicle(@NonNull Rental rental);

    Rental getCurrentRental();
}
