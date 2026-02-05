package com.carousell.marketplace.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Domain model for a Marketplace Category.
 * Encapsulating the category as an entity allows for future extensibility.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private String name;
}