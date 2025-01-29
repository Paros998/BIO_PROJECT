package psk.bio.car.rental.infrastructure.spring.delivery.http;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import psk.bio.car.rental.api.common.paging.PageRequest;
import psk.bio.car.rental.api.common.paging.PageResponse;
import psk.bio.car.rental.api.employees.EmployeeModel;
import psk.bio.car.rental.api.employees.FinishFirstLoginRequest;
import psk.bio.car.rental.infrastructure.data.common.paging.PageMapper;
import psk.bio.car.rental.infrastructure.data.services.EmployeeServiceImpl;

import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/employees")
@RestController
public class EmployeeHttpEndpoint {
    private final EmployeeServiceImpl employeeService;

    @GetMapping
    public PageResponse<EmployeeModel> fetchEmployees(final @RequestParam(required = false, defaultValue = "1") Integer page,
                                                      final @RequestParam(required = false, defaultValue = "10") Integer pageLimit,
                                                      final @RequestParam(required = false, defaultValue = "desc") String sortDir,
                                                      final @RequestParam(required = false, defaultValue = "firstName") String sortBy) {
        final PageRequest pageRequest = PageMapper.toPageRequest(page, pageLimit, sortDir, sortBy);
        return employeeService.fetchEmployees(pageRequest);
    }

    @PostMapping("/{userId}/activate")
    public void setEmployeeActiveState(final @PathVariable("userId") UUID employeeId, final @RequestParam Boolean setActive) {
        employeeService.setEmployeeActiveState(employeeId, setActive);
    }

    // --------------------------------------

    @PostMapping("/finish-first-login")
    public void finishFirstLogin(final @Valid @RequestBody FinishFirstLoginRequest request) {
        employeeService.finishFirstLogin(request.getEmployeeId(), request.getPassword());
    }
}
