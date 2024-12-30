package psk.bio.car.rental.application.employee;

import java.util.UUID;

public interface EmployeeService {
    void finishFirstLogin(UUID employeeId, String newPassword);
}
