package psk.bio.car.rental.api.insure;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import psk.bio.car.rental.api.employees.CreateEmployeeRequest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class CreateEmployeeRequestTest {
    
    @Test
void shouldThrowNullPointerExceptionWhenFirstNameIsNull() {
    assertThrows(NullPointerException.class, () -> {
        CreateEmployeeRequest.builder()
                .lastName("Doe")
                .phoneNumber("1234567890")
                .nationalId("AB123456")
                .build();
    });
}
@Test
void shouldThrowNullPointerExceptionWhenLastNameIsNull() {
    assertThrows(NullPointerException.class, () -> {
        CreateEmployeeRequest.builder()
                .firstName("John")
                .phoneNumber("1234567890")
                .nationalId("AB123456")
                .build();
    });
}
@Test
void shouldThrowNullPointerExceptionWhenPhoneNumberIsNull() {
    assertThrows(NullPointerException.class, () -> {
        CreateEmployeeRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .nationalId("AB123456")
                .build();
    });
}

}
