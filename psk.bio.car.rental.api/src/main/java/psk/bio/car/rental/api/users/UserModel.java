package psk.bio.car.rental.api.users;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
@EqualsAndHashCode
public class UserModel implements Serializable {
    private UUID userId;
    private String username;
    private Boolean enabled;
    private Boolean firstLogin;
    private UserRole role;
}
