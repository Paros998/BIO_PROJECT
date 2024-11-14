package psk.bio.car.rental.application.rental;

public interface RentalProjection {
    String getRentalId();

    RentalState getState();

    void finishRental();
}
