package psk.bio.car.rental.application.rental;

import lombok.NonNull;

import java.util.UUID;

public interface RentalService {
    UUID rentVehicle(@NonNull UUID vehicleId, @NonNull UUID clientId, @NonNull Integer numberOfDays);
}
