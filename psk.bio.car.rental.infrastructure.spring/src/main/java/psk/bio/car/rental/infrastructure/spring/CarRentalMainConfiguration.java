package psk.bio.car.rental.infrastructure.spring;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(value = {
        "psk.bio.car.rental.application",
        "psk.bio.car.rental.infrastructure.spring.configuration",
        "psk.bio.car.rental.infrastructure.spring.delivery.http",
        "psk.bio.car.rental.infrastructure.spring.error.handling",
        "psk.bio.car.rental.infrastructure.data.services",
})
@EnableJpaRepositories(basePackages = "psk.bio.car.rental.infrastructure.data")
@EntityScan(basePackages = "psk.bio.car.rental.infrastructure.data")
@EnableTransactionManagement
@EnableAsync
@EnableScheduling
public class CarRentalMainConfiguration {
}
