package com.piisw.UrbanTicketSystem.domain.model;

public enum UserPermission {
    CLIENT_READ("client:read"),
    CLIENT_WRITE("client:write");

    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
