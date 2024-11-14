package psk.bio.car.rental.application.vehicle;

import java.math.BigDecimal;

public interface InRepairVehicleProjection {
    ReadyToRentVehicleProjection finishRepairs(BigDecimal totalCost);
}
