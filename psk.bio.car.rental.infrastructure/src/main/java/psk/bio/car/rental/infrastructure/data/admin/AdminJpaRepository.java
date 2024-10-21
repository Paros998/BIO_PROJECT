package psk.bio.car.rental.infrastructure.data.admin;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

@SuppressWarnings("unused")
public interface AdminJpaRepository extends JpaRepository<AdminEntity, UUID> {
}