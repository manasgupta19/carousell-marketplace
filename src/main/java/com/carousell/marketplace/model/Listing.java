package com.carousell.marketplace.model;

public class Listing {
    private final int id;
    private final String title;
    private final String description;
    private final double price;
    private final String createdAt;
    private final String category;
    private final String owner;

    public Listing(int id, String title, String description, double price,
                   String createdAt, String category, String owner) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.createdAt = createdAt;
        this.category = category;
        this.owner = owner;
    }

    // Add these missing getters to resolve the IDE errors
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getId() { return id; }
    public double getPrice() { return price; }
    public String getCreatedAt() { return createdAt; }
    public String getCategory() { return category; }
    public String getOwner() { return owner; }
}