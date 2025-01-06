package psk.bio.car.rental.application.security;

import lombok.NonNull;
import psk.bio.car.rental.application.security.exceptions.BusinessException;

import java.util.UUID;

public interface UserContextValidator {
    void checkUserPerformingAction(@NonNull UUID userId) throws BusinessException;
}
