package psk.bio.car.rental.application.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public interface UserProjection extends UserDetails {
    String ROLE_PREFIX = "ROLE_";

    UUID getUserId();

    UserRole getRole();

    Collection<Permission> getPermissions();

    String getEmail();

    Boolean isActive();

    @Override
    default Collection<? extends GrantedAuthority> getAuthorities() {
        final Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority((ROLE_PREFIX + getRole().name())));
        getPermissions().forEach(permission -> authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + permission.getName())));

        return authorities;
    }

    @Override
    default String getUsername() {
        return getEmail();
    }

    @Override
    String getPassword();

    @Override
    default boolean isAccountNonLocked() {
        return !isActive();
    }

    @Override
    default boolean isEnabled() {
        return isActive();
    }
}