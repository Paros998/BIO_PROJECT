package psk.bio.car.rental.api.vehicles;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import psk.bio.car.rental.api.vehicles.AddVehicleRequest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)

class AddVehicleRequestTest {

    @Test
void shouldCreateValidAddVehicleRequestWithAllFieldsPopulated() {
    // Given
    String model = "Tesla Model S";
    String plate = "ABC123";
    String color = "Red";
    Integer yearOfProduction = 2023;
    BigDecimal rentPerDayPrice = new BigDecimal("100.00");

    // When
    AddVehicleRequest request = AddVehicleRequest.builder()
            .model(model)
            .plate(plate)
            .color(color)
            .yearOfProduction(yearOfProduction)
            .rentPerDayPrice(rentPerDayPrice)
            .build();

    // Then
    assertNotNull(request);
    assertEquals(model, request.getModel());
    assertEquals(plate, request.getPlate());
    assertEquals(color, request.getColor());
    assertEquals(yearOfProduction, request.getYearOfProduction());
    assertEquals(rentPerDayPrice, request.getRentPerDayPrice());
}
@Test
void shouldCreateAddVehicleRequestWithNullValues() {
    // When
    AddVehicleRequest request = AddVehicleRequest.builder().build();

    // Then
    assertNotNull(request);
    assertNull(request.getModel());
    assertNull(request.getPlate());
    assertNull(request.getColor());
    assertNull(request.getYearOfProduction());
    assertNull(request.getRentPerDayPrice());
}
@Test
void shouldCreateAddVehicleRequestWithVeryLongModelName() {
    // Given
    String longModelName = "A".repeat(1000);
    String plate = "XYZ789";
    String color = "Blue";
    Integer yearOfProduction = 2022;
    BigDecimal rentPerDayPrice = new BigDecimal("150.00");

    // When
    AddVehicleRequest request = AddVehicleRequest.builder()
            .model(longModelName)
            .plate(plate)
            .color(color)
            .yearOfProduction(yearOfProduction)
            .rentPerDayPrice(rentPerDayPrice)
            .build();

    // Then
    assertNotNull(request);
    assertEquals(longModelName, request.getModel());
    assertEquals(1000, request.getModel().length());
    assertEquals(plate, request.getPlate());
    assertEquals(color, request.getColor());
    assertEquals(yearOfProduction, request.getYearOfProduction());
    assertEquals(rentPerDayPrice, request.getRentPerDayPrice());
}
@Test
void shouldCreateAddVehicleRequestWithFutureYearOfProduction() {
    // Given
    String model = "Future Car";
    String plate = "FUT2050";
    String color = "Silver";
    Integer futureYear = LocalDate.now().getYear() + 10; // 10 years in the future
    BigDecimal rentPerDayPrice = new BigDecimal("250.00");

    // When
    AddVehicleRequest request = AddVehicleRequest.builder()
            .model(model)
            .plate(plate)
            .color(color)
            .yearOfProduction(futureYear)
            .rentPerDayPrice(rentPerDayPrice)
            .build();

    // Then
    assertNotNull(request);
    assertEquals(model, request.getModel());
    assertEquals(plate, request.getPlate());
    assertEquals(color, request.getColor());
    assertEquals(futureYear, request.getYearOfProduction());
    assertTrue(request.getYearOfProduction() > LocalDate.now().getYear());
    assertEquals(rentPerDayPrice, request.getRentPerDayPrice());
}
@Test
void shouldProperlyImplementEqualsAndHashCodeForIdenticalObjects() {
    // Given
    String model = "Tesla Model 3";
    String plate = "ABC123";
    String color = "White";
    Integer yearOfProduction = 2023;
    BigDecimal rentPerDayPrice = new BigDecimal("120.00");

    AddVehicleRequest request1 = AddVehicleRequest.builder()
            .model(model)
            .plate(plate)
            .color(color)
            .yearOfProduction(yearOfProduction)
            .rentPerDayPrice(rentPerDayPrice)
            .build();

    AddVehicleRequest request2 = AddVehicleRequest.builder()
            .model(model)
            .plate(plate)
            .color(color)
            .yearOfProduction(yearOfProduction)
            .rentPerDayPrice(rentPerDayPrice)
            .build();

    // Then
    assertEquals(request1, request2);
    assertEquals(request1.hashCode(), request2.hashCode());
}
}
