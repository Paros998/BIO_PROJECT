package psk.bio.car.rental.application.user;

import lombok.NonNull;
import psk.bio.car.rental.api.security.FinishRegisterRequest;

import java.util.UUID;

public interface ClientService {
    UUID registerNewClient(String email, String password);

    void finishRegistration(UUID userId, @NonNull FinishRegisterRequest request);
}
