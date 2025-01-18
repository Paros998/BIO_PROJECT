package psk.bio.car.rental.infrastructure.data.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import psk.bio.car.rental.api.employees.CreateEmployeeRequest;
import psk.bio.car.rental.application.security.UserRole;
import psk.bio.car.rental.application.user.UserRepository;
import psk.bio.car.rental.infrastructure.data.employee.EmployeeEntity;
import psk.bio.car.rental.infrastructure.data.employee.EmployeeJpaRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmployeeJpaRepository employeeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminServiceImpl adminService;


    @Test
    void shouldCreateNewEmployeeWhenGivenValidInputData() {
        // Arrange
        CreateEmployeeRequest request = new CreateEmployeeRequest();
        request.setUsername("john.doe@example.com");
        request.setPassword("password123");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setNationalId("1234567890");
        request.setPhoneNumber("1234567890");

        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(employeeRepository.save(any(EmployeeEntity.class))).thenAnswer(invocation -> {
            EmployeeEntity savedEmployee = invocation.getArgument(0);
            savedEmployee.setUserId(UUID.randomUUID());
            return savedEmployee;
        });

        // Act
        UUID result = adminService.addEmployee(request);

        // Assert
        assertNotNull(result);
        verify(userRepository).findByUsername(request.getUsername());
        verify(passwordEncoder).encode(request.getPassword());
        verify(employeeRepository).save(argThat(employee ->
                employee.getRole() == UserRole.EMPLOYEE &&
                        employee.getEmail().equals(request.getUsername()) &&
                        employee.getPassword().equals("encodedPassword") &&
                        employee.getEmployeeIdentifier().startsWith("EMP_") &&
                        employee.getFirstName().equals(request.getFirstName()) &&
                        employee.getLastName().equals(request.getLastName()) &&
                        employee.getNationalId().equals(request.getNationalId()) &&
                        employee.getPhoneNumber().equals(request.getPhoneNumber()) &&
                        employee.getEnabled() &&
                        !employee.getFirstLoginDone()
        ));
    }

    @Test
    void shouldGenerateUniqueEmployeeIdentifierForEachNewEmployee() {
        // Arrange
        CreateEmployeeRequest request1 = new CreateEmployeeRequest();
        request1.setUsername("employee1@example.com");
        request1.setPassword("password123");
        request1.setFirstName("John");
        request1.setLastName("Doe");
        request1.setNationalId("1234567890");
        request1.setPhoneNumber("1234567890");

        CreateEmployeeRequest request2 = new CreateEmployeeRequest();
        request2.setUsername("employee2@example.com");
        request2.setPassword("password456");
        request2.setFirstName("Jane");
        request2.setLastName("Smith");
        request2.setNationalId("0987654321");
        request2.setPhoneNumber("0987654321");

        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(employeeRepository.save(any())).thenAnswer(invocation -> {
            EmployeeEntity savedEmployee = invocation.getArgument(0);
            savedEmployee.setUserId(UUID.randomUUID());
            return savedEmployee;
        });

        // Act
        UUID result1 = adminService.addEmployee(request1);
        UUID result2 = adminService.addEmployee(request2);

        // Assert
        assertNotNull(result1);
        assertNotNull(result2);
        verify(employeeRepository, times(2)).save(argThat(employee ->
                employee.getEmployeeIdentifier().startsWith("EMP_") &&
                        employee.getEmployeeIdentifier().length() == 40 // 4 for "EMP_" + 36 for UUID
        ));
        verify(employeeRepository).save(argThat(employee ->
                employee.getEmail().equals("employee1@example.com")
        ));
        verify(employeeRepository).save(argThat(employee ->
                employee.getEmail().equals("employee2@example.com")
        ));
    }

    @Test
    void shouldValidateEmailFormatBeforeCreatingEmployee() {
        // Arrange
        CreateEmployeeRequest request = new CreateEmployeeRequest();
        request.setUsername("invalid-email");
        request.setPassword("password123");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setNationalId("1234567890");
        request.setPhoneNumber("1234567890");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> adminService.addEmployee(request));
        verify(userRepository, never()).findByUsername(any());
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void shouldHandleSpecialCharactersInInputFieldsCorrectly() {
        // Arrange
        CreateEmployeeRequest request = new CreateEmployeeRequest();
        request.setUsername("test.user+special@example.com");
        request.setPassword("p@ssw0rd!#$%");
        request.setFirstName("John-Émile");
        request.setLastName("O'Connor-Müller");
        request.setNationalId("AB-123456/7");
        request.setPhoneNumber("+1 (555) 123-4567");

        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(employeeRepository.save(any(EmployeeEntity.class))).thenAnswer(invocation -> {
            EmployeeEntity savedEmployee = invocation.getArgument(0);
            savedEmployee.setUserId(UUID.randomUUID());
            return savedEmployee;
        });

        // Act
        UUID result = adminService.addEmployee(request);

        // Assert
        assertNotNull(result);
        verify(userRepository).findByUsername(request.getUsername());
        verify(passwordEncoder).encode(request.getPassword());
        verify(employeeRepository).save(argThat(employee ->
                employee.getEmail().equals("test.user+special@example.com") &&
                        employee.getPassword().equals("encodedPassword") &&
                        employee.getFirstName().equals("John-Émile") &&
                        employee.getLastName().equals("O'Connor-Müller") &&
                        employee.getNationalId().equals("AB-123456/7") &&
                        employee.getPhoneNumber().equals("+1 (555) 123-4567")
        ));
    }

    @Test
    void shouldHandlePasswordEncoderException() {
        // Arrange
        CreateEmployeeRequest request = new CreateEmployeeRequest();
        request.setUsername("test@example.com");
        request.setPassword("password123");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setNationalId("1234567890");
        request.setPhoneNumber("1234567890");

        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenThrow(new IllegalArgumentException("Invalid password"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> adminService.addEmployee(request));
        verify(userRepository).findByUsername(request.getUsername());
        verify(passwordEncoder).encode(request.getPassword());
        verify(employeeRepository, never()).save(any());
    }
}
