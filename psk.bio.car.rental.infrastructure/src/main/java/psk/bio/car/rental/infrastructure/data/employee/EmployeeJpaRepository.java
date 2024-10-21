package psk.bio.car.rental.infrastructure.data.employee;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

@SuppressWarnings("unused")
public interface EmployeeJpaRepository extends JpaRepository<EmployeeEntity, UUID> {
}
