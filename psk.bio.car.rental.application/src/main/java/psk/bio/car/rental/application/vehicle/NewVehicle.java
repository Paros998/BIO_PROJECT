package psk.bio.car.rental.application.vehicle;

import java.time.LocalDateTime;

public interface NewVehicle extends Vehicle {
    ReadyToRentVehicle insureVehicle(String insuranceId, LocalDateTime dueDate);
}
