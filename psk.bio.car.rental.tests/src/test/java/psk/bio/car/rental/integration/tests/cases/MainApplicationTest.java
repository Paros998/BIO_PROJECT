package psk.bio.car.rental.integration.tests.cases;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import psk.bio.car.rental.integration.tests.CarRentalIntegrationTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@CarRentalIntegrationTest
class MainApplicationTest {
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void shouldAppStartupCorrectly() {
        assertNotNull(applicationContext);
    }
}