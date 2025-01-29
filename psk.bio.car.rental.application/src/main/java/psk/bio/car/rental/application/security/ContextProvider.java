package psk.bio.car.rental.application.security;

import psk.bio.car.rental.application.security.exceptions.BusinessException;
import psk.bio.car.rental.application.user.UserProjection;

public interface ContextProvider {
    UserProjection getCurrentUser() throws BusinessException;
}
