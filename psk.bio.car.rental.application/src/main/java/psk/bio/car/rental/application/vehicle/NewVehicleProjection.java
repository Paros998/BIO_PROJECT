package psk.bio.car.rental.application.vehicle;

import java.time.LocalDateTime;

public interface NewVehicleProjection {
    ReadyToRentVehicleProjection insureVehicle(String externalInsuranceId, LocalDateTime dueDate);
}
