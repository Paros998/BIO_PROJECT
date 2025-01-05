package psk.bio.car.rental.infrastructure.data.payments;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

@SuppressWarnings("unused")
public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, UUID> {
}
