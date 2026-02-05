package com.carousell.marketplace.repository;

/**
 * Interface for commands that modify data.
 * Updated return type to boolean to allow the caller to detect registration
 * success/failure and prevent race conditions.
 */
public interface MarketplaceWriter {
    /**
     * @param username The unique identifier for the user.
     * @return true if the user was successfully registered, false if they already exist.
     */
    boolean registerUser(String username);

    int addListing(String owner, String title, String desc, double price, String cat, String time);

    void deleteListing(int id);
}