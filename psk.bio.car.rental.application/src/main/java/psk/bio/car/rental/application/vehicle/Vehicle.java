package psk.bio.car.rental.application.vehicle;

import java.math.BigDecimal;
import java.time.Year;
import java.util.UUID;

public interface Vehicle {
    UUID getVehicleId();

    String getPlate();

    String getModel();

    String getColor();

    Year getYearOfProduction();

    VehicleState getState();

    BigDecimal getRentPrice();

    boolean isRented();
}
