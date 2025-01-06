package psk.bio.car.rental.spring.boot.standalone.starters.users;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import psk.bio.car.rental.application.user.UserRepository;
import psk.bio.car.rental.infrastructure.data.client.ClientJpaRepository;
import psk.bio.car.rental.infrastructure.data.employee.EmployeeJpaRepository;

import java.util.stream.Stream;

@SuppressWarnings("checkstyle:MagicNumber")
@Log4j2
@RequiredArgsConstructor
@Order(100)
public class UsersInitializer implements ApplicationRunner {
    private final PasswordEncoder passwordEncoder;
    private final ClientJpaRepository clientRepository;
    private final EmployeeJpaRepository employeeRepository;
    private final UserRepository userRepository;
    private final UsersToAddConfig config;

    @Override
    @Transactional
    public void run(final ApplicationArguments args) {
        log.info("{} running", this.getClass().getSimpleName());
        config.getAdmins().forEach(admin -> admin.setPassword(passwordEncoder.encode(admin.getPassword())));
        config.getClients().forEach(client -> client.setPassword(passwordEncoder.encode(client.getPassword())));
        config.getEmployees().forEach(employee -> employee.setPassword(passwordEncoder.encode(employee.getPassword())));

        final var clientsToAdd = config.getClients().stream()
                .filter(client -> userRepository.findByUsername(client.getEmail()).isEmpty()).toList();
        final var adminsToAdd = config.getAdmins().stream()
                .filter(admin -> userRepository.findByUsername(admin.getEmail()).isEmpty()).toList();
        final var employeesToAdd = config.getEmployees().stream()
                .filter(employee -> userRepository.findByUsername(employee.getEmail()).isEmpty()).toList();

        clientRepository.saveAll(clientsToAdd);
        log.info("Initialized clients: {}", clientsToAdd);

        employeeRepository.saveAll(adminsToAdd);
        employeeRepository.saveAll(employeesToAdd);
        log.info("Initialized employees: {}", Stream.concat(adminsToAdd.stream(), employeesToAdd.stream()).toList());
    }
}
