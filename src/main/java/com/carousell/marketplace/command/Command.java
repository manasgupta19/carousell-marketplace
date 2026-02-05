package com.carousell.marketplace.command;

import com.carousell.marketplace.repository.MarketplaceRepository;

public interface Command {
    /**
     * @param args The split input parts (e.g., ["CREATE_LISTING", "user1", "'Phone'", ...])
     * @return The string to be printed to STDOUT (Success or Error message)
     */
    String execute(String[] args);
}