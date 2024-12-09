package psk.bio.car.rental.infrastructure.data.admin;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

@SuppressWarnings("unused")
public interface AdminJpaRepository extends JpaRepository<AdminEntity, UUID> {
}
