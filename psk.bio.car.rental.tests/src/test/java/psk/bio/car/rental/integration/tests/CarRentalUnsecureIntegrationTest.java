package psk.bio.car.rental.integration.tests;

import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@CarRentalIntegrationTest
@ActiveProfiles({"test", "UNSECURE"})
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CarRentalUnsecureIntegrationTest {
}
