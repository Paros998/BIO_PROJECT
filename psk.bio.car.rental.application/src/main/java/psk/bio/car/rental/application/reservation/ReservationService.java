package psk.bio.car.rental.application.reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationService {
  Optional<ReservationProjection> createReservation(ReservationProjection reservationProjection);
  Optional<ReservationProjection> updateReservation(ReservationProjection reservationProjection);
  Optional<ReservationProjection> deleteReservation(ReservationProjection reservationProjection);
  Optional<ReservationProjection> getReservationById(Long id);
  List<ReservationProjection> getAllReservations();

}
