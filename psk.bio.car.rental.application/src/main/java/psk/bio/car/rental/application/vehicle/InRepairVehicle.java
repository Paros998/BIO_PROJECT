package psk.bio.car.rental.application.vehicle;

import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface InRepairVehicle extends Vehicle {
    ReadyToRentVehicle finishRepairs(@NonNull BigDecimal totalCost, @NonNull LocalDateTime dueDate);

    ReadyToRentVehicle finishRepairs(@NonNull BigDecimal totalCost, @NonNull LocalDateTime dueDate, String chargedClientId);
}
