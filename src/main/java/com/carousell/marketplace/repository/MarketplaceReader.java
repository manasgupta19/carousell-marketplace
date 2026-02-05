package com.carousell.marketplace.repository;

import com.carousell.marketplace.model.Listing;
import java.util.List;

public interface MarketplaceReader {
    boolean userExists(String username);
    Listing getListing(int id);
    List<Listing> getAllListings();
    String getCachedTopCategory();
}