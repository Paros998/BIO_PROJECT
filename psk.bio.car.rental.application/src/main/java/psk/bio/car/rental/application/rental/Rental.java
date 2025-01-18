package psk.bio.car.rental.application.rental;

import org.springframework.lang.Nullable;
import psk.bio.car.rental.application.vehicle.Vehicle;

import java.time.LocalDateTime;
import java.util.UUID;

public interface Rental {
    UUID getRentalId();

    UUID getClientId();

    Vehicle getVehicle();

    UUID getVehicleId();

    @Nullable UUID getApprovingEmployeeId();

    LocalDateTime getRentStartDate();

    LocalDateTime getRentEndDate();

    Boolean areAllPaymentsFeesPaid();

    RentalState getState();

    void finishRental();
}
