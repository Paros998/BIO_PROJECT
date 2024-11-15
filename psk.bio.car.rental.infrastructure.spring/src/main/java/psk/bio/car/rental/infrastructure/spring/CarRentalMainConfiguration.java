package psk.bio.car.rental.infrastructure.spring;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(value = {
        "psk.bio.car.rental.application",
        "psk.bio.car.rental.infrastructure.spring.configuration",
        "psk.bio.car.rental.infrastructure.spring.delivery.http",
        "psk.bio.car.rental.infrastructure.spring.error.handling",
})
@EnableJpaRepositories(basePackages = "psk.bio.car.rental.infrastructure.data")
@EntityScan(basePackages = "psk.bio.car.rental.infrastructure.data")
@EnableTransactionManagement
public class CarRentalMainConfiguration {
}
