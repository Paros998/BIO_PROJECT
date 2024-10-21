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

    protected String password;

    @Column(unique = true, nullable = false)
    protected String email;

    protected String firstName;

    protected String lastName;

    protected String phoneNumber;

    protected String nationalId;

    protected Boolean enabled;

    @Enumerated(EnumType.STRING)
    protected UserRole role;

    @ElementCollection(fetch = FetchType.EAGER, targetClass = Permission.class)
    protected List<Permission> permissions = new ArrayList<>();

    @Override
    public Boolean isActive() {
        return enabled;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        UserEntity that = (UserEntity) o;
        return getUserId() != null && Objects.equals(getUserId(), that.getUserId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}