package psk.bio.car.rental.infrastructure.spring.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import psk.bio.car.rental.application.profiles.ApplicationProfile;

@Profile(ApplicationProfile.BCRYPT_ENCRYPTION)
@Configuration
public class BcryptConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(31);
    }
}
