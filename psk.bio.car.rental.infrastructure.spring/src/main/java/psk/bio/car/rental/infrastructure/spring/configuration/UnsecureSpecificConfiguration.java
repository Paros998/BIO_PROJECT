package psk.bio.car.rental.infrastructure.spring.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import psk.bio.car.rental.application.profiles.ApplicationProfile;
import psk.bio.car.rental.application.user.UserRepository;
import psk.bio.car.rental.infrastructure.data.user.unsecure.UnsecureUserRepository;

@Profile(ApplicationProfile.UNSECURE)
@Configuration
public class UnsecureSpecificConfiguration {

    @Bean
    public UserRepository userRepository() {
        return new UnsecureUserRepository();
    }
}
