package com.carousell.marketplace.command;

import com.carousell.marketplace.repository.MarketplaceReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Handles high-performance retrieval of the top category.
 * Leverages the eager caching strategy in the repository for O(1) performance.
 */
@Component("GET_TOP_CATEGORY")
@RequiredArgsConstructor
public class GetTopCategoryCommand implements Command {

    private final MarketplaceReader reader;

    @Override
    public String execute(String[] args) {
        if (args.length < 2) return "Error - invalid arguments";

        String username = args[1];
        if (!reader.userExists(username)) return "Error - unknown user";

        String topCategory = reader.getCachedTopCategory();
        return (topCategory == null || topCategory.isEmpty()) ? "" : topCategory;
    }
}