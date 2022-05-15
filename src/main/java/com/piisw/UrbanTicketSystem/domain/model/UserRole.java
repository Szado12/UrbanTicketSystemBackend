package com.piisw.UrbanTicketSystem.domain.model;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.piisw.UrbanTicketSystem.domain.model.UserPermission.*;

public enum UserRole {
    CLIENT(Sets.newHashSet(CLIENT_READ, CLIENT_WRITE)),
    //OAUTH_CLIENT(Sets.newHashSet(CLIENT_READ, CLIENT_WRITE, RESERVATION_READ, RESERVATION_WRITE)),
    //PREREGISTERED_CLIENT(Sets.newHashSet(CLIENT_READ, CLIENT_WRITE)),
    STAFF(Sets.newHashSet(CLIENT_READ)),
    ADMIN(Sets.newHashSet());

    private final Set<UserPermission> permissions;

    UserRole(Set<UserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<UserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream().map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());

        permissions.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
        return permissions;
    }
}
