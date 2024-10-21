package psk.bio.car.rental.infrastructure.data.user;

import jakarta.persistence.*;
import lombok.*;
import psk.bio.car.rental.application.security.Permission;
import psk.bio.car.rental.application.user.UserProjection;
import psk.bio.car.rental.application.security.UserRole;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "users")
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity implements UserProjection {
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    @Column(
            nullable = false,
            updatable = false
    )
    protected UUID userId;

    protected String password;

    protected String email;

    protected String firstName;

    protected String lastName;

    protected String phoneNumber;

    protected String nationalId;

    protected Boolean enabled;

    @Enumerated(EnumType.STRING)
    protected UserRole role;

    @ElementCollection(fetch = FetchType.EAGER, targetClass = Permission.class)
    protected List<Permission> permissions;

    @Override
    public Boolean isActive() {
        return enabled;
    }
}