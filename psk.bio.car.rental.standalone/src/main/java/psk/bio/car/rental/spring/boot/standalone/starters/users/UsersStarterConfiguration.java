package psk.bio.car.rental.spring.boot.standalone.starters.users;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import psk.bio.car.rental.infrastructure.data.client.ClientJpaRepository;
import psk.bio.car.rental.infrastructure.data.employee.EmployeeJpaRepository;

@Configuration
@ConditionalOnProperty(prefix = "car-rental.users.starter", value = "enabled", havingValue = "true")
public class UsersStarterConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "car-rental.users.starter.data")
    public UsersToAddConfig usersToAddConfig() {
        return new UsersToAddConfig();
    }

    @Bean
    public UsersInitializer usersInitializer(
            final @Lazy PasswordEncoder passwordEncoder,
            final @Lazy ClientJpaRepository clientJpaRepository,
            final @Lazy EmployeeJpaRepository employeeJpaRepository) {
        return new UsersInitializer(passwordEncoder, clientJpaRepository, employeeJpaRepository, usersToAddConfig());
    }
}
