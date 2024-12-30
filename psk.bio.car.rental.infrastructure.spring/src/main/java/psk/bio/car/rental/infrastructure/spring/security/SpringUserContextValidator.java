package psk.bio.car.rental.infrastructure.spring.security;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import psk.bio.car.rental.application.security.UserContextValidator;
import psk.bio.car.rental.application.security.exceptions.BusinessException;
import psk.bio.car.rental.application.security.exceptions.BusinessExceptionFactory;
import psk.bio.car.rental.application.user.UserProjection;
import psk.bio.car.rental.application.user.UserRepository;

import java.util.Optional;
import java.util.UUID;

import static psk.bio.car.rental.application.security.exceptions.BusinessExceptionCodes.USER_PERFORMING_ACTION_NOT_EXISTS;
import static psk.bio.car.rental.application.security.exceptions.BusinessExceptionCodes.USER_PERFORMING_ACTION_NOT_ON_THEMSELVES;

@RequiredArgsConstructor
public class SpringUserContextValidator implements UserContextValidator {
    private final UserRepository userRepository;
    private final SecurityContextHolderStrategy securityContextHolder = SecurityContextHolder
            .getContextHolderStrategy();

    @Override
    public void checkUserPerformingAction(final @NonNull UUID userId) throws BusinessException {
        SecurityContext context = securityContextHolder.getContext();
        if (context == null) {
            return;
        }

        Object username = context.getAuthentication().getPrincipal();
        Optional<UserProjection> userProjection = userRepository.findByUsername((String) username);
        if (userProjection.isEmpty()) {
            throw BusinessExceptionFactory.instantiateBusinessException(USER_PERFORMING_ACTION_NOT_EXISTS);
        }

        if (!userProjection.get().getUserId().equals(userId)) {
            throw BusinessExceptionFactory.instantiateBusinessException(USER_PERFORMING_ACTION_NOT_ON_THEMSELVES);
        }
    }
}
