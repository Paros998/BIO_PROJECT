package psk.bio.car.rental.infrastructure.spring.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import psk.bio.car.rental.application.user.UserRepository;
import psk.bio.car.rental.infrastructure.spring.authentication.UserDaoAuthenticationProvider;
import psk.bio.car.rental.infrastructure.spring.error.handling.CustomFilterAdvice;
import psk.bio.car.rental.infrastructure.spring.filters.formlogin.FormLoginAuthenticationFilter;
import psk.bio.car.rental.infrastructure.spring.filters.jwt.JwtTokenFilter;
import psk.bio.car.rental.infrastructure.spring.filters.jwt.JwtTokenRefresher;

@Configuration
public class AuthenticationConfiguration {
    private @Value("${jwt.secret}") String jwtSecret;

    @Bean
    public AuthenticationEntryPointFailureHandler authenticationEntryPointFailureHandler(
            final @Qualifier("customHttpAdvice") AuthenticationEntryPoint entryPoint) {
        return new AuthenticationEntryPointFailureHandler(entryPoint);
    }

    @Bean
    public FormLoginAuthenticationFilter formLoginAuthenticationFilter(
            final UserRepository userRepository,
            final AuthenticationManager authenticationManager,
            final AuthenticationEntryPointFailureHandler failureHandler,
            final @Qualifier("customHttpAdvice") CustomFilterAdvice customFilterAdvice
    ) {
        final var filter = new FormLoginAuthenticationFilter(authenticationManager,
                userRepository,
                failureHandler,
                customFilterAdvice,
                jwtSecret);
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    @Bean
    public JwtTokenFilter jwtTokenFilter(
            final JwtTokenRefresher jwtTokenRefresher,
            final @Qualifier("customHttpAdvice") CustomFilterAdvice customFilterAdvice) {
        return new JwtTokenFilter(jwtSecret, jwtTokenRefresher, customFilterAdvice);
    }

    @Bean
    public JwtTokenRefresher jwtTokenRefresher(final UserRepository userRepository) {
        return new JwtTokenRefresher(userRepository, jwtSecret);
    }

    @Bean
    public UserDaoAuthenticationProvider authenticationProvider(final UserRepository userRepository,
                                                                final PasswordEncoder passwordEncoder) {
        return new UserDaoAuthenticationProvider(userRepository, passwordEncoder);
    }
}
