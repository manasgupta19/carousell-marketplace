package com.carousell.marketplace.command;

import com.carousell.marketplace.command.sort.ListingSortStrategy;
import com.carousell.marketplace.command.sort.SortStrategyFactory;
import com.carousell.marketplace.model.Listing;
import com.carousell.marketplace.repository.MarketplaceReader;
import com.carousell.marketplace.util.Parser;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles the retrieval and sorting of listings within a specific category.
 * Demonstrates the Strategy Pattern for flexible sorting logic.
 */
public class GetCategoryCommand implements Command {
    private final MarketplaceReader reader;

    public GetCategoryCommand(MarketplaceReader reader) {
        this.reader = reader;
    }

    @Override
    public String execute(String[] args) {
        // 1. Basic Argument Validation
        if (args.length < 5) return "Error - invalid arguments";

        // 2. Authentication Check
        String username = args[1];
        if (!reader.userExists(username)) return "Error - unknown user";

        // 3. Category Filtering
        String categoryName = Parser.stripQuotes(args[2]);
        List<Listing> filtered = reader.getAllListings().stream()
                .filter(l -> l.getCategory().equalsIgnoreCase(categoryName))
                .collect(Collectors.toList());

        // 4. Requirement: Silence for empty or non-existent category
        if (filtered.isEmpty()) {
            return "Error - category not found";
        }

        // 5. Strategy Pattern for Sorting
        // Decouples the command from specific sorting algorithms (Price vs Time)
        try {
            String sortKey = args[3];
            String sortOrder = args[4];
            ListingSortStrategy strategy = SortStrategyFactory.getStrategy(sortKey, sortOrder);
            filtered.sort(strategy.getComparator());
        } catch (Exception e) {
            return "Error - invalid sort parameters";
        }

        // 6. Formatted Output
        return filtered.stream()
                .map(this::formatOutput)
                .collect(Collectors.joining("\n"));
    }

    /**
     * Formats the listing according to the pipe-separated specification.
     * Price is formatted as an integer (%.0f) to match expected output.
     */
    private String formatOutput(Listing l) {
        return String.format("%s|%s|%.0f|%s|%s|%s",
                l.getTitle(),
                l.getDescription(),
                l.getPrice(),
                l.getCreatedAt(),
                l.getCategory(),
                l.getOwner());
    }
}