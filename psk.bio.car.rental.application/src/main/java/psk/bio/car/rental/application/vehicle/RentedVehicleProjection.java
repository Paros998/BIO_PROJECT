package psk.bio.car.rental.application.vehicle;

import psk.bio.car.rental.application.rental.RentalProjection;

public interface RentedVehicleProjection {
    ReadyToRentVehicleProjection returnVehicle(String clientId);

    RentalProjection getRental();
}
