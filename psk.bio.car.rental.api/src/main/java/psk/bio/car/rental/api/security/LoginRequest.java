package psk.bio.car.rental.api.security;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public final class LoginRequest {
    @NonNull
    private String username;

    @NonNull
    private String password;
}
