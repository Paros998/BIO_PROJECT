package psk.bio.car.rental.application.vehicle;

import lombok.NonNull;

import java.math.BigDecimal;
import java.nio.file.attribute.UserPrincipal;
import java.time.LocalDate;

public interface InRepairVehicle extends Vehicle {
    ReadyToRentVehicle finishRepairsAndMakeReadyToRent(@NonNull BigDecimal totalCost, @NonNull LocalDate dueDate);

    ReadyToRentVehicle finishRepairsAndMakeReadyToRent(@NonNull BigDecimal totalCost, @NonNull LocalDate dueDate, @NonNull UserPrincipal client);

    NotInsuredVehicle finishRepairsAndRequireInsurance(@NonNull BigDecimal totalCost, @NonNull LocalDate dueDate);

    NotInsuredVehicle finishRepairsAndRequireInsurance(@NonNull BigDecimal totalCost, @NonNull LocalDate dueDate, @NonNull UserPrincipal client);
}
