package psk.bio.car.rental.application.security;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public final class Permission implements Serializable {
    public static final Permission EXAMPLE = new Permission("EXAMPLE");

    private String name;
}