package psk.bio.car.rental.api.security;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
@EqualsAndHashCode
public class RegisterRequest implements Serializable {
    @NonNull
    private String username;

    @NonNull
    private String password;
}
