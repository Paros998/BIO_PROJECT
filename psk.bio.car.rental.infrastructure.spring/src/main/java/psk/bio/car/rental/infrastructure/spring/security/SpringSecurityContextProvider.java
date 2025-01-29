package psk.bio.car.rental.infrastructure.spring.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import psk.bio.car.rental.application.security.ContextProvider;
import psk.bio.car.rental.application.security.exceptions.BusinessException;
import psk.bio.car.rental.application.security.exceptions.BusinessExceptionFactory;
import psk.bio.car.rental.application.user.UserProjection;
import psk.bio.car.rental.application.user.UserRepository;

import static psk.bio.car.rental.application.security.exceptions.BusinessExceptionCodes.USER_NOT_LOGGED_IN;
import static psk.bio.car.rental.application.security.exceptions.BusinessExceptionCodes.USER_PERFORMING_ACTION_NOT_EXISTS;

@RequiredArgsConstructor
public class SpringSecurityContextProvider implements ContextProvider {
    private final UserRepository userRepository;
    private final SecurityContextHolderStrategy securityContextHolder = SecurityContextHolder
            .getContextHolderStrategy();

    @Override
    public UserProjection getCurrentUser() throws BusinessException {
        SecurityContext context = securityContextHolder.getContext();
        if (context == null) {
            throw BusinessExceptionFactory.instantiateBusinessException(USER_NOT_LOGGED_IN);
        }

        Object username = context.getAuthentication().getPrincipal();
        return userRepository.findByUsername((String) username)
                .orElseThrow(() -> BusinessExceptionFactory.instantiateBusinessException(USER_PERFORMING_ACTION_NOT_EXISTS));
    }
}
