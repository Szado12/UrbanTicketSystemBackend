package com.piisw.UrbanTicketSystem.security;

public enum ApplicationUserPermission {
    CLIENT_READ("client:read"),
    CLIENT_WRITE("client:write"),
    RESERVATION_READ("reservation:read"),
    RESERVATION_WRITE("reservation:write"),
    HOTEL_WRITE("hotel:write");

    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
