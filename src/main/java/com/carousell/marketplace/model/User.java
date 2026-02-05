package com.carousell.marketplace.model;

public class User {
    private final String username; // Case-insensitive unique identifier

    public User(String username) {
        this.username = username;
    }

    public String getUsername() { return username; }
}