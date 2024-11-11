package psk.bio.car.rental.infrastructure.data.reservation;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import psk.bio.car.rental.application.reservation.ReservationRepository;

@Repository
public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, UUID> ,
    ReservationRepository {
  @Override
  default Optional<ReservationEntity> save(ReservationEntity reservation) {
    return Optional.of(this.saveAndFlush(reservation));
  }

  @Override
  default Optional<ReservationEntity> findById(UUID id) {
    return Optional.of(this.getOne(id));
  }
}
