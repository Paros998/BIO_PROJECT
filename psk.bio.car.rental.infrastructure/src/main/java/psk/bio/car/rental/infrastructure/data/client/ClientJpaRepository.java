package psk.bio.car.rental.infrastructure.data.client;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

@SuppressWarnings("unused")
public interface ClientJpaRepository extends JpaRepository<ClientEntity, UUID> {
}
