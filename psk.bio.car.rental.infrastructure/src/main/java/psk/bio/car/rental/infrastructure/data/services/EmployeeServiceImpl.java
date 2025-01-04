package psk.bio.car.rental.infrastructure.data.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import psk.bio.car.rental.api.common.paging.PageRequest;
import psk.bio.car.rental.api.common.paging.PageResponse;
import psk.bio.car.rental.api.employees.EmployeeModel;
import psk.bio.car.rental.application.employee.EmployeeService;
import psk.bio.car.rental.application.security.UserContextValidator;
import psk.bio.car.rental.infrastructure.data.admin.AdminEntity;
import psk.bio.car.rental.infrastructure.data.common.paging.PageMapper;
import psk.bio.car.rental.infrastructure.data.common.paging.SpringPageRequest;
import psk.bio.car.rental.infrastructure.data.common.paging.SpringPageResponse;
import psk.bio.car.rental.infrastructure.data.employee.EmployeeEntity;
import psk.bio.car.rental.infrastructure.data.employee.EmployeeJpaRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeJpaRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserContextValidator userContextValidator;

    @Override
    @Transactional
    public void finishFirstLogin(final @NonNull UUID employeeId, final @NonNull String newPassword) {
        userContextValidator.checkUserPerformingAction(employeeId);

        final EmployeeEntity employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));

        employee.setPassword(passwordEncoder.encode(newPassword));
        employee.setFirstLoginDone(Boolean.TRUE);
        employeeRepository.save(employee);
    }

    @Override
    public PageResponse<EmployeeModel> fetchEmployees(final PageRequest pageRequest) {
        final SpringPageRequest springPageRequest = PageMapper.toSpringPageRequest(pageRequest);
        final SpringPageResponse<EmployeeEntity> employees =
                new SpringPageResponse<>(employeeRepository.findAll(springPageRequest.getRequest(EmployeeEntity.class)));
        return PageMapper.toPageResponse(employees, this::toEmployeeModel);
    }

    @Override
    @Transactional
    public void setEmployeeActiveState(final @NonNull UUID employeeId, final boolean newState) {
        final EmployeeEntity employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));
        employee.setEnabled(newState);
        employeeRepository.save(employee);
    }

    private EmployeeModel toEmployeeModel(final @NonNull EmployeeEntity employee) {
        return EmployeeModel.builder()
                .userId(employee.getUserId())
                .employeeId(employee.getEmployeeIdentifier())
                .email(employee.getEmail())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .phoneNumber(employee.getPhoneNumber())
                .nationalId(employee.getNationalId())
                .isActive(employee.isActive())
                .firstLoginDone(employee.getFirstLoginDone())
                .isAdmin(employee instanceof AdminEntity)
                .build();
    }
}
