package com.example.Bank_REST.util;

public enum UserRole {
    USER("USER"),
    ADMIN("ADMIN");

    private String role;

    public String getRole() {
        return role;
    }

    UserRole(String role) {
        this.role = role;
    }
}
