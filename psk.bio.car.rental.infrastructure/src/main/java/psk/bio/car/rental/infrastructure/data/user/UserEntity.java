package psk.bio.car.rental.infrastructure.data.user;

import jakarta.persistence.*;
import lombok.*;
import psk.bio.car.rental.application.security.Permission;
import psk.bio.car.rental.application.security.UserProjection;
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
    private UUID userId;

    private String password;

    private String email;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String nationalId;

    private Boolean enabled;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @ElementCollection(fetch = FetchType.EAGER, targetClass = Permission.class)
    private List<Permission> permissions;

    @Override
    public Boolean isActive() {
        return enabled;
    }
}