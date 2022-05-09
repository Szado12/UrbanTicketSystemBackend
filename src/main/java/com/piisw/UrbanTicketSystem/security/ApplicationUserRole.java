package com.piisw.UrbanTicketSystem.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.piisw.UrbanTicketSystem.security.ApplicationUserPermission.*;

public enum ApplicationUserRole {
    CLIENT(Sets.newHashSet(CLIENT_READ, CLIENT_WRITE, RESERVATION_READ, RESERVATION_WRITE)),
    FACEBOOK_CLIENT(Sets.newHashSet(CLIENT_READ, CLIENT_WRITE, RESERVATION_READ, RESERVATION_WRITE)),
    PREREGISTERED_CLIENT(Sets.newHashSet(CLIENT_READ, CLIENT_WRITE)),
    STAFF(Sets.newHashSet(CLIENT_READ, RESERVATION_READ, RESERVATION_WRITE)),
    ADMIN(Sets.newHashSet(HOTEL_WRITE));

    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream().map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());

        permissions.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
        return permissions;
    }
}
