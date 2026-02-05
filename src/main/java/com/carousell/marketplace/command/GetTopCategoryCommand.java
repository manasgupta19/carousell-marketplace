package com.carousell.marketplace.command;

import com.carousell.marketplace.repository.MarketplaceReader;

/**
 * Handles the O(1) retrieval of the top category.
 * Demonstrates high performance for read-heavy operations.
 */
public class GetTopCategoryCommand implements Command {
    private final MarketplaceReader reader;

    public GetTopCategoryCommand(MarketplaceReader reader) {
        this.reader = reader;
    }

    @Override
    public String execute(String[] args) {
        // Format: GET_TOP_CATEGORY <user>
        if (args.length < 2) return "Error - invalid arguments";

        String username = args[1];
        if (!reader.userExists(username)) return "Error - unknown user";

        // Fetching the pre-calculated value from the repository cache
        String topCategory = reader.getCachedTopCategory();

        return (topCategory == null) ? "" : topCategory;
    }
}