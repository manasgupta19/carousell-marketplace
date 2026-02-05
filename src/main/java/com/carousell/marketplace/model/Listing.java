package com.carousell.marketplace.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Domain model for a Marketplace Listing.
 * Uses Lombok to eliminate boilerplate and provide a clean Builder pattern.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Listing {
    private int id;
    private String title;
    private String description;
    private double price;
    private String createdAt;
    private String category;
    private String owner;
}