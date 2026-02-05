package com.carousell.marketplace.command;

import com.carousell.marketplace.command.sort.ListingSortStrategy;
import com.carousell.marketplace.command.sort.SortStrategyFactory;
import com.carousell.marketplace.model.Listing;
import com.carousell.marketplace.repository.MarketplaceReader;
import com.carousell.marketplace.util.Parser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles category-based filtering and sorting.
 * Employs the Strategy Pattern to decouple sorting logic from the command.
 */
@Component("GET_CATEGORY")
@RequiredArgsConstructor
public class GetCategoryCommand implements Command {

    private final MarketplaceReader reader;

    @Override
    public String execute(String[] args) {
        if (args.length < 5) return "Error - invalid arguments";

        if (!reader.userExists(args[1])) return "Error - unknown user";

        String categoryName = Parser.stripQuotes(args[2]);
        List<Listing> filtered = reader.getAllListings().stream()
                .filter(l -> l.getCategory().equalsIgnoreCase(categoryName))
                .collect(Collectors.toList());

        if (filtered.isEmpty()) return "Error - category not found";

        try {
            ListingSortStrategy strategy = SortStrategyFactory.getStrategy(args[3], args[4]);
            filtered.sort(strategy.getComparator());
        } catch (Exception e) {
            return "Error - invalid sort parameters";
        }

        return filtered.stream()
                .map(this::formatOutput)
                .collect(Collectors.joining("\n"));
    }

    private String formatOutput(Listing l) {
        return String.format("%s|%s|%.0f|%s|%s|%s",
                l.getTitle(), l.getDescription(), l.getPrice(),
                l.getCreatedAt(), l.getCategory(), l.getOwner());
    }
}