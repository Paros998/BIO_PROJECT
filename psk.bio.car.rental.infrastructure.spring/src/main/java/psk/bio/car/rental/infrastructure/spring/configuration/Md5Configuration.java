package psk.bio.car.rental.infrastructure.spring.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import psk.bio.car.rental.application.profiles.ApplicationProfile;
import psk.bio.car.rental.application.security.utils.Md5PasswordEncoder;

@Profile(ApplicationProfile.MD5_ENCRYPTION)
@Configuration
public class Md5Configuration {
    @Value("${security.encryption.md5.salt}")
    private String md5Salt;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Md5PasswordEncoder(md5Salt);
    }
}