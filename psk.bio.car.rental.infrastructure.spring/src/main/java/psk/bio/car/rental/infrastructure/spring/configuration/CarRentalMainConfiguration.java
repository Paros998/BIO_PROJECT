package psk.bio.car.rental.infrastructure.spring.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(value = {
        "psk.bio.car.rental.application",
        "psk.bio.car.rental.infrastructure.data",
        "psk.bio.car.rental.infrastructure.spring"
})
public class CarRentalMainConfiguration {
}
