package com.carousell.marketplace.repository;

import com.carousell.marketplace.model.Listing;
import com.carousell.marketplace.model.User;

import java.util.List;

// Interface for commands that only need to read data
public interface MarketplaceReader {
    boolean userExists(String username);
    Listing getListing(int id);
    List<Listing> getAllListings();
    String getCachedTopCategory();
}
