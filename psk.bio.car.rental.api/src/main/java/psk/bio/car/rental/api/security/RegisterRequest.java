package psk.bio.car.rental.api.security;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public final class RegisterRequest implements Serializable {
    @NonNull
    private String username;

    @NonNull
    private String password;
}
