package test.psk.bio.car.rental.api.rentals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import psk.bio.car.rental.api.rentals.CreateVehicleRentalRequest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CreateVehicleRentalRequestTest {
@Test
void shouldCreateValidCreateVehicleRentalRequest() {
    UUID vehicleId = UUID.randomUUID();
    UUID clientId = UUID.randomUUID();
    Integer numberOfDays = 7;

    CreateVehicleRentalRequest request = CreateVehicleRentalRequest.builder()
            .vehicleId(vehicleId)
            .clientId(clientId)
            .numberOfDays(numberOfDays)
            .build();

    assertNotNull(request);
    assertEquals(vehicleId, request.getVehicleId());
    assertEquals(clientId, request.getClientId());
    assertEquals(numberOfDays, request.getNumberOfDays());
}
@Test
void shouldThrowExceptionWhenVehicleIdIsNull() {
    UUID clientId = UUID.randomUUID();
    Integer numberOfDays = 7;

    assertThrows(NullPointerException.class, () -> {
        CreateVehicleRentalRequest.builder()
                .vehicleId(null)
                .clientId(clientId)
                .numberOfDays(numberOfDays)
                .build();
    });
}

@Test
void shouldThrowExceptionWhenNumberOfDaysIsNull() {
    UUID vehicleId = UUID.randomUUID();
    UUID clientId = UUID.randomUUID();

    assertThrows(NullPointerException.class, () -> {
        CreateVehicleRentalRequest.builder()
                .vehicleId(vehicleId)
                .clientId(clientId)
                .numberOfDays(null)
                .build();
    });
}

@Test
void shouldCreateEqualObjectsWhenAllFieldsHaveSameValues() {
    UUID vehicleId = UUID.randomUUID();
    UUID clientId = UUID.randomUUID();
    Integer numberOfDays = 5;

    CreateVehicleRentalRequest request1 = CreateVehicleRentalRequest.builder()
            .vehicleId(vehicleId)
            .clientId(clientId)
            .numberOfDays(numberOfDays)
            .build();

    CreateVehicleRentalRequest request2 = CreateVehicleRentalRequest.builder()
            .vehicleId(vehicleId)
            .clientId(clientId)
            .numberOfDays(numberOfDays)
            .build();

    assertEquals(request1, request2);
    assertEquals(request1.hashCode(), request2.hashCode());
}

@Test
void shouldGenerateCorrectToStringRepresentation() {
    UUID vehicleId = UUID.randomUUID();
    UUID clientId = UUID.randomUUID();
    Integer numberOfDays = 7;

    CreateVehicleRentalRequest request = CreateVehicleRentalRequest.builder()
            .vehicleId(vehicleId)
            .clientId(clientId)
            .numberOfDays(numberOfDays)
            .build();

    String expectedToString = "CreateVehicleRentalRequest(vehicleId=" + vehicleId + ", clientId=" + clientId + ", numberOfDays=" + numberOfDays + ")";
    assertEquals(expectedToString, request.toString());
}


}
