package test.psk.bio.car.rental.api.vehicles;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import psk.bio.car.rental.api.rentals.RentalModel;
import psk.bio.car.rental.api.vehicles.RentedVehicle;
import psk.bio.car.rental.api.vehicles.VehicleModel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class RentedVehicleTest {

    private RentedVehicle rentedVehicle;
    private VehicleModel vehicle;
    private RentalModel rental;

    
    
    
    @Test
void shouldReturnTrueWhenComparingTwoIdenticalRentedVehicleObjects() {
    VehicleModel vehicle = new VehicleModel();
    RentalModel rental = new RentalModel();

    RentedVehicle rentedVehicle1 = new RentedVehicle(vehicle, rental);
    RentedVehicle rentedVehicle2 = new RentedVehicle(vehicle, rental);

    assertEquals(rentedVehicle1, rentedVehicle2);
    assertTrue(rentedVehicle1.equals(rentedVehicle2));
}
@Test
void shouldAllowSettingAndGettingVehicleAndRentalProperties() {
    VehicleModel vehicle = new VehicleModel();
    RentalModel rental = new RentalModel();

    RentedVehicle rentedVehicle = new RentedVehicle();
    rentedVehicle.setVehicle(vehicle);
    rentedVehicle.setRental(rental);

    assertEquals(vehicle, rentedVehicle.getVehicle());
    assertEquals(rental, rentedVehicle.getRental());
}

}
