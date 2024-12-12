package psk.bio.car.rental.application.vehicle;

import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface InRepairVehicle extends Vehicle {
    ReadyToRentVehicle finishRepairs(@NonNull BigDecimal totalCost, @NonNull LocalDate dueDate);

    ReadyToRentVehicle finishRepairs(@NonNull BigDecimal totalCost, @NonNull LocalDate dueDate, String chargedClientId);
}
