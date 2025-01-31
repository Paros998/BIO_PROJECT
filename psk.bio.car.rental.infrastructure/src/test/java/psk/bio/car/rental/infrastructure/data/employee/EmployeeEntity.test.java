package psk.bio.car.rental.infrastructure.data.employee;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import psk.bio.car.rental.application.security.UserRole;
import psk.bio.car.rental.infrastructure.data.payments.PaymentEntity;
import psk.bio.car.rental.infrastructure.data.rentals.RentalEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmployeeEntityTest {
@Test
void shouldReturnFalseWhenComparingWithNull() {
    EmployeeEntity employeeEntity = new EmployeeEntity();
    assertFalse(employeeEntity.equals(null));
}

@Test
void shouldReturnFalseWhenCreatedPaymentsIsDifferent() {
    EmployeeEntity employee1 = new EmployeeEntity();
    employee1.setEmployeeIdentifier("EMP001");
    employee1.setCreatedPayments(List.of(new PaymentEntity()));

    EmployeeEntity employee2 = new EmployeeEntity();
    employee2.setEmployeeIdentifier("EMP001");
    employee2.setCreatedPayments(new ArrayList<>());

    assertFalse(employee1.equals(employee2));
}@Test
void shouldReturnFalseWhenRentedVehiclesIsDifferent() {
    EmployeeEntity employee1 = new EmployeeEntity();
    employee1.setEmployeeIdentifier("EMP001");
    employee1.setRentedVehicles(List.of(new RentalEntity()));

    EmployeeEntity employee2 = new EmployeeEntity();
    employee2.setEmployeeIdentifier("EMP001");
    employee2.setRentedVehicles(new ArrayList<>());

    assertFalse(employee1.equals(employee2));
}
@Test
void shouldReturnDifferentHashCodesForDifferentEmployeeIdentifiers() {
    EmployeeEntity employee1 = new EmployeeEntity();
    employee1.setEmployeeIdentifier("EMP001");

    EmployeeEntity employee2 = new EmployeeEntity();
    employee2.setEmployeeIdentifier("EMP002");

    assertNotEquals(employee1.hashCode(), employee2.hashCode());
}
@Test
void shouldReturnConsistentHashCodeForUnchangedObject() {
    EmployeeEntity employee = new EmployeeEntity();
    employee.setEmployeeIdentifier("EMP001");
    employee.setRentedVehicles(List.of(new RentalEntity()));
    employee.setCreatedPayments(List.of(new PaymentEntity()));

    int hashCode1 = employee.hashCode();
    int hashCode2 = employee.hashCode();
    int hashCode3 = employee.hashCode();

    assertEquals(hashCode1, hashCode2);
    assertEquals(hashCode2, hashCode3);
}
@Test
void shouldHandleNullRentedVehiclesListInHashCodeCalculation() {
    EmployeeEntity employee = new EmployeeEntity();
    employee.setEmployeeIdentifier("EMP001");
    employee.setRentedVehicles(null);
    employee.setCreatedPayments(new ArrayList<>());

    int hashCode = employee.hashCode();

    assertNotEquals(0, hashCode);
}

}
