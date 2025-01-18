package psk.bio.car.rental.application.rental;

import org.springframework.lang.Nullable;

import lombok.NonNull;
import psk.bio.car.rental.application.user.UserProjection;
import psk.bio.car.rental.application.vehicle.Vehicle;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public interface Rental {
    BigDecimal OVER_DUE_MODIFIER = new BigDecimal("1.5");

    UUID getRentalId();

    UUID getClientId();

    Vehicle getVehicle();

    UUID getVehicleId();

    @Nullable UUID getApprovingEmployeeId();

    LocalDateTime getRentStartDate();

    LocalDateTime getRentEndDate();

    Boolean areAllPaymentsFeesPaid();

    RentalState getState();

    void finishRental(@NonNull UserProjection participatingEmployee);
}
