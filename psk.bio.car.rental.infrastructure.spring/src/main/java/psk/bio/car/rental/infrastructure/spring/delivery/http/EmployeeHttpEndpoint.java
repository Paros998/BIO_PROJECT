package psk.bio.car.rental.infrastructure.spring.delivery.http;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import psk.bio.car.rental.api.employees.FinishFirstLoginRequest;
import psk.bio.car.rental.application.employee.EmployeeService;

@RequiredArgsConstructor
@RequestMapping("/api/employees")
@RestController
public class EmployeeHttpEndpoint {
    private final EmployeeService employeeService;

    @PostMapping("/finish-first-login")
    public void finishFirstLogin(final @Valid @RequestBody FinishFirstLoginRequest request) {
        employeeService.finishFirstLogin(request.getEmployeeId(), request.getPassword());
    }
}
