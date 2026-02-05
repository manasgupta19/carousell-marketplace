package com.carousell.marketplace.repository;

// Interface for commands that modify data
public interface MarketplaceWriter {
    void registerUser(String username);
    int addListing(String owner, String title, String desc, double price, String cat, String time);
    void deleteListing(int id);
}
