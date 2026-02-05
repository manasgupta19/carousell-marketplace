package com.carousell.marketplace.model;

import java.util.Objects;

/**
 * Represents a logical category in the marketplace.
 * Using an entity instead of a plain String allows for future extensibility
 * (e.g., category descriptions, parent-child relationships, or metadata).
 */
public class Category {
    private final String name;

    /**
     * @param name The display name of the category.
     */
    public Category(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be empty");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        // Categories are unique by their case-insensitive name
        return name.equalsIgnoreCase(category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase());
    }

    @Override
    public String toString() {
        return name;
    }
}