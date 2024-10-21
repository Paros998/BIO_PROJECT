package psk.bio.car.rental.spring.boot.standalone.runners;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import psk.bio.car.rental.infrastructure.data.client.ClientJpaRepository;
import psk.bio.car.rental.infrastructure.data.employee.EmployeeJpaRepository;

@Log4j2
@RequiredArgsConstructor
public class UsersInitializer implements ApplicationRunner {
    private final PasswordEncoder passwordEncoder;
    private final ClientJpaRepository clientRepository;
    private final EmployeeJpaRepository employeeRepository;
    private final UsersToAddConfig config;

    @Override
    @Transactional
    public void run(final ApplicationArguments args) {
        log.info("{} running", this.getClass().getSimpleName());
        config.getAdmins().forEach(admin -> admin.setPassword(passwordEncoder.encode(admin.getPassword())));
        config.getClients().forEach(client -> client.setPassword(passwordEncoder.encode(client.getPassword())));
        config.getEmployees().forEach(employee -> employee.setPassword(passwordEncoder.encode(employee.getPassword())));

        clientRepository.saveAll(config.getClients());
        log.info("Initialized clients: {}", clientRepository.findAll());

        employeeRepository.saveAll(config.getAdmins());
        employeeRepository.saveAll(config.getEmployees());
        log.info("Initialized employees: {}", employeeRepository.findAll());
    }
}