package psk.bio.car.rental.spring.boot.standalone.starters.users;

import lombok.Data;
import lombok.NoArgsConstructor;
import psk.bio.car.rental.infrastructure.data.admin.AdminEntity;
import psk.bio.car.rental.infrastructure.data.client.ClientEntity;
import psk.bio.car.rental.infrastructure.data.employee.EmployeeEntity;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class UsersToAddConfig {
    private List<AdminEntity> admins = new ArrayList<>();
    private List<EmployeeEntity> employees = new ArrayList<>();
    private List<ClientEntity> clients = new ArrayList<>();
}
