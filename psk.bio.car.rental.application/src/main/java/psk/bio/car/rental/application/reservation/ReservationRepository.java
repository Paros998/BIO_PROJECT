package psk.bio.car.rental.application.reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import psk.bio.car.rental.application.user.UserProjection;

public interface ReservationRepository {
  UserProjection findClient();
  UserProjection findEmployee();
  LocalDate findDateOdRent();
  LocalDate findDateOfReturn();
  Optional<ReservationProjection> findReservationById(UUID id);
  List<ReservationProjection> findAllReservations();
  ReservationProjection save(ReservationProjection reservationProjection);
  ReservationProjection update(ReservationProjection reservationProjection);
  void delete(ReservationProjection reservationProjection);

}
