package psk.bio.car.rental.application.vehicle;

import java.time.Year;

public interface Vehicle {
    String getVehicleId();

    String getPlate();

    String getModel();

    String getColor();

    Year getYearOfProduction();

    VehicleState getState();

    boolean isRented();
}
