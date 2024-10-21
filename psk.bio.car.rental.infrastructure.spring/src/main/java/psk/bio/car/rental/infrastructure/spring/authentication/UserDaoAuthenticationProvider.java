package psk.bio.car.rental.infrastructure.spring.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import psk.bio.car.rental.application.user.UserProjection;
import psk.bio.car.rental.application.user.UserRepository;

import java.util.Optional;

@RequiredArgsConstructor
public class UserDaoAuthenticationProvider implements AuthenticationManager {
    private final UserRepository userDao;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        final String username = authentication.getName();
        final String password = (String) authentication.getCredentials();

        final Optional<UserProjection> user = userDao.findByUsername(username);

        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return new UsernamePasswordAuthenticationToken(username, password, user.get().getAuthorities());
        } else {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

}
