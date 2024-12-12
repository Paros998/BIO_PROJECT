package psk.bio.car.rental.spring.boot.standalone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import psk.bio.car.rental.infrastructure.spring.CarRentalMainConfiguration;

@SpringBootApplication
@ComponentScan(basePackages = {
        "psk.bio.car.rental.spring.boot.standalone.starters.users",
        "psk.bio.car.rental.spring.boot.standalone.starters.vehicles"
})
@Import({
        CarRentalMainConfiguration.class
})
public class CarRentalApplication {
    public static void main(final String[] args) {
        SpringApplication.run(CarRentalApplication.class, args);
    }
}
