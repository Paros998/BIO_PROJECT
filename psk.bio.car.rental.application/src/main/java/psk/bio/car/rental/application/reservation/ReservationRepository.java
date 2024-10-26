package psk.bio.car.rental.application.reservation;

import java.time.LocalDate;
import psk.bio.car.rental.application.user.UserProjection;

public interface ReservationRepository {
  UserProjection findClient();
  UserProjection findEmployee();
  LocalDate findDateOdRent();
  LocalDate findDateOfReturn();
}
