package psk.bio.car.rental.infrastructure.spring.delivery.http;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import psk.bio.car.rental.api.employees.CreateEmployeeRequest;
import psk.bio.car.rental.infrastructure.data.services.AdminServiceImpl;

import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/admins")
@RestController
public class AdminHttpEndpoint {
    private final AdminServiceImpl adminService;

    @PostMapping("/new-employee")
    public UUID addEmployee(final @Valid @RequestBody CreateEmployeeRequest request) {
        return adminService.addEmployee(request);
    }
}
