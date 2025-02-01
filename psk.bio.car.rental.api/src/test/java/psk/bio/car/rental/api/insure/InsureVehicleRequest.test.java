package psk.bio.car.rental.api.insure;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import psk.bio.car.rental.api.employees.vehicles.management.InsureVehicleRequest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class InsureVehicleRequestTest {
    
    @Test
void shouldThrowNullPointerExceptionWhenInsuranceIdIsNull() {
    assertThrows(NullPointerException.class, () -> {
        InsureVehicleRequest.builder()
                .insuranceId(null)
                .bankAccountNumber("123456789")
                .insuranceCost(BigDecimal.valueOf(1000))
                .dueDate(LocalDate.now().plusMonths(1))
                .build();
    });
}

}
