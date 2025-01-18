package psk.bio.car.rental.infrastructure.data.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import psk.bio.car.rental.api.employees.CreateEmployeeRequest;
import psk.bio.car.rental.application.admin.AdminService;
import psk.bio.car.rental.application.security.UserRole;
import psk.bio.car.rental.application.security.exceptions.BusinessExceptionFactory;
import psk.bio.car.rental.application.user.UserProjection;
import psk.bio.car.rental.application.user.UserRepository;
import psk.bio.car.rental.infrastructure.data.employee.EmployeeEntity;
import psk.bio.car.rental.infrastructure.data.employee.EmployeeJpaRepository;

import java.util.Optional;
import java.util.UUID;

import static psk.bio.car.rental.application.security.exceptions.BusinessExceptionCodes.USER_WITH_SAME_USERNAME_ALREADY_EXISTS;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final EmployeeJpaRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UUID addEmployee(final @NonNull CreateEmployeeRequest request) {
        Optional<UserProjection> user = userRepository.findByUsername(request.getUsername());
        if (user.isPresent()) {
            throw BusinessExceptionFactory.instantiateBusinessException(USER_WITH_SAME_USERNAME_ALREADY_EXISTS);
        }

            final EmployeeEntity employee = EmployeeEntity.builder()
                .role(UserRole.EMPLOYEE)
                .email(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .employeeIdentifier("EMP_" + UUID.randomUUID())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .nationalId(request.getNationalId())
                .phoneNumber(request.getPhoneNumber())
                .enabled(Boolean.TRUE)
                .firstLoginDone(Boolean.FALSE)
                .build();
            return employeeRepository.save(employee).getUserId();
    }
}
