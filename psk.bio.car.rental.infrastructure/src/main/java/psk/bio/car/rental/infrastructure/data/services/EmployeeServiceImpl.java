package psk.bio.car.rental.infrastructure.data.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import psk.bio.car.rental.application.employee.EmployeeService;
import psk.bio.car.rental.application.security.UserContextValidator;
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
}
