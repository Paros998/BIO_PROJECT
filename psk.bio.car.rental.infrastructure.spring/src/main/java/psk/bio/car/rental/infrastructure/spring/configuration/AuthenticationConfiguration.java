package psk.bio.car.rental.infrastructure.spring.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import psk.bio.car.rental.application.user.UserRepository;
import psk.bio.car.rental.infrastructure.spring.authentication.UserDaoAuthenticationProvider;
import psk.bio.car.rental.infrastructure.spring.filters.formlogin.FormLoginAuthenticationFilter;
import psk.bio.car.rental.infrastructure.spring.filters.jwt.JwtTokenFilter;
import psk.bio.car.rental.infrastructure.spring.filters.jwt.JwtTokenRefresher;

@Configuration
public class AuthenticationConfiguration {
    private @Value("${jwt.secret}") String jwtSecret;

    @Bean
    public FormLoginAuthenticationFilter formLoginAuthenticationFilter(final UserRepository userRepository,
                                                                       final AuthenticationManager authenticationManager) {
        final var formLoginAuthenticationFilter = new FormLoginAuthenticationFilter(authenticationManager, userRepository, jwtSecret);
        formLoginAuthenticationFilter.setAuthenticationManager(authenticationManager);
        return formLoginAuthenticationFilter;
    }

    @Bean
    public JwtTokenFilter jwtTokenFilter(final JwtTokenRefresher jwtTokenRefresher) {
        return new JwtTokenFilter(jwtSecret, jwtTokenRefresher);
    }

    @Bean
    public JwtTokenRefresher jwtTokenRefresher(final UserRepository userRepository) {
        return new JwtTokenRefresher(userRepository, jwtSecret);
    }

    @Bean
    public UserDaoAuthenticationProvider authenticationProvider(final UserRepository userRepository, final PasswordEncoder passwordEncoder) {
        return new UserDaoAuthenticationProvider(userRepository, passwordEncoder);
    }
}
