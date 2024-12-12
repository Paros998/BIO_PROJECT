package psk.bio.car.rental.application.vehicle;

import java.time.LocalDate;

public interface NewVehicle extends Vehicle {
    ReadyToRentVehicle insureVehicle(String insuranceId, LocalDate dueDate);
}
