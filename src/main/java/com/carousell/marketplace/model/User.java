package com.carousell.marketplace.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

/**
 * Domain model for a Marketplace User.
 * Uses Lombok @Value to ensure immutability for user identifiers.
 */
@Value
@Builder
@AllArgsConstructor
public class User {
    // Unique username, treated as case-insensitive in the repository layer
    String username;
}