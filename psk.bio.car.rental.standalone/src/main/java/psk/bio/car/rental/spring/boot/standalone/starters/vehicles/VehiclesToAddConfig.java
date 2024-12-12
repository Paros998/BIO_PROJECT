package psk.bio.car.rental.spring.boot.standalone.starters.vehicles;

import lombok.Data;
import lombok.NoArgsConstructor;
import psk.bio.car.rental.infrastructure.data.vehicle.VehicleEntity;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class VehiclesToAddConfig {
    private List<VehicleEntity> vehicles = new ArrayList<>();
}
