package psk.bio.car.rental.infrastructure.data.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import psk.bio.car.rental.api.employees.EmployeeModel;
import psk.bio.car.rental.application.security.UserContextValidator;
import psk.bio.car.rental.infrastructure.data.employee.EmployeeEntity;
import psk.bio.car.rental.infrastructure.data.employee.EmployeeJpaRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Mock
    private EmployeeJpaRepository employeeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserContextValidator userContextValidator;

    @Test
    void shouldSuccessfullyFinishFirstLoginAndUpdateEmployeePasswordAndStatus() {
        // Given
        UUID employeeId = UUID.randomUUID();
        String newPassword = "newPassword123";
        EmployeeEntity employee = new EmployeeEntity();
        employee.setFirstLoginDone(false);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedPassword");

        // When
        employeeService.finishFirstLogin(employeeId, newPassword);

        // Then
        verify(userContextValidator).checkUserPerformingAction(employeeId);
        verify(passwordEncoder).encode(newPassword);
        verify(employeeRepository).save(employee);

        assertEquals("encodedPassword", employee.getPassword());
        assertTrue(employee.getFirstLoginDone());
    }

    @Test
    void shouldValidateUserContextBeforeFinishingFirstLogin() {
        // Given
        UUID employeeId = UUID.randomUUID();
        String newPassword = "newPassword123";

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        // When & Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> employeeService.finishFirstLogin(employeeId, newPassword));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Employee not found", exception.getReason());
        verify(userContextValidator).checkUserPerformingAction(employeeId);
        verify(employeeRepository).findById(employeeId);
    }


@Test
void shouldSetEmployeeActiveStateToFalseAndSaveChanges() {
    // Given
    UUID employeeId = UUID.randomUUID();
    EmployeeEntity employee = new EmployeeEntity();
    employee.setEnabled(true);

    when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

    // When
    employeeService.setEmployeeActiveState(employeeId, false);

    // Then
    assertFalse(employee.isActive());
    verify(employeeRepository).findById(employeeId);
    verify(employeeRepository).save(employee);
}

    @Test
    void shouldMapEmployeeEntityToEmployeeModel() throws Exception {
        // Given
        EmployeeEntity employee = new EmployeeEntity();
        employee.setUserId(UUID.randomUUID());
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");

        // Użycie refleksji
        var method = EmployeeServiceImpl.class.getDeclaredMethod("toEmployeeModel", EmployeeEntity.class);
        method.setAccessible(true);

        // When
        EmployeeModel result = (EmployeeModel) method.invoke(employeeService, employee);

        // Then
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("john.doe@example.com", result.getEmail());
    }


}
