package psk.bio.car.rental.infrastructure.spring.security;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import psk.bio.car.rental.application.security.ContextProvider;
import psk.bio.car.rental.application.security.UserContextValidator;
import psk.bio.car.rental.application.security.exceptions.BusinessException;
import psk.bio.car.rental.application.security.exceptions.BusinessExceptionFactory;
import psk.bio.car.rental.application.user.UserProjection;

import java.util.UUID;

import static psk.bio.car.rental.application.security.exceptions.BusinessExceptionCodes.USER_PERFORMING_ACTION_NOT_ON_THEMSELVES;

@RequiredArgsConstructor
public class SpringUserContextValidator implements UserContextValidator {
    private final ContextProvider contextProvider;

    @Override
    public void checkUserPerformingAction(final @NonNull UUID userId) throws BusinessException {
        final UserProjection userProjection = contextProvider.getCurrentUser();

        if (!userProjection.getUserId().equals(userId)) {
            throw BusinessExceptionFactory.instantiateBusinessException(USER_PERFORMING_ACTION_NOT_ON_THEMSELVES);
        }
    }
}
