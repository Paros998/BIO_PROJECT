package psk.bio.car.rental.integration.tests;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import psk.bio.car.rental.integration.tests.configuration.CarRentalTestConfiguration;
import psk.bio.car.rental.spring.boot.standalone.CarRentalApplication;

import java.lang.annotation.*;

@Inherited
@SpringBootTest(
        classes = CarRentalApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ExtendWith(SpringExtension.class)
@ActiveProfiles({"test", "SECURE"})
@Import({
        CarRentalTestConfiguration.class
})
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CarRentalIntegrationTest {
}
