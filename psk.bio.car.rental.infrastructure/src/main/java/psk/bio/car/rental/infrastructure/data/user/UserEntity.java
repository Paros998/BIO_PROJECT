package psk.bio.car.rental.infrastructure.data.user;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.proxy.HibernateProxy;
import psk.bio.car.rental.application.security.Permission;
import psk.bio.car.rental.application.security.UserRole;
import psk.bio.car.rental.application.user.UserProjection;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "users")
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
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

    @ToString.Exclude
    protected String password;

    @Column(unique = true, nullable = false)
    protected String email;

    protected String firstName;

    protected String lastName;

    protected String phoneNumber;

    protected String nationalId;

    protected Boolean enabled;

    protected Boolean firstLoginDone;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    protected UserRole role;

    @ElementCollection(fetch = FetchType.EAGER, targetClass = Permission.class)
    protected List<Permission> permissions = new ArrayList<>();

    @Override
    public Boolean isActive() {
        return enabled;
    }

    @Override
    public Boolean isFirstLogin() {
        return !firstLoginDone;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserEntity that = (UserEntity) o;
        return Objects.equals(userId, that.userId) && Objects.equals(password, that.password)
                && Objects.equals(email, that.email) && Objects.equals(firstName, that.firstName)
                && Objects.equals(lastName, that.lastName) && Objects.equals(phoneNumber, that.phoneNumber)
                && Objects.equals(nationalId, that.nationalId) && Objects.equals(enabled, that.enabled)
                && Objects.equals(firstLoginDone, that.firstLoginDone) && role == that.role
                && Objects.equals(permissions, that.permissions);
    }

    @Override
    public int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this)
                .getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
