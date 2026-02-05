package com.carousell.marketplace.command;

import com.carousell.marketplace.command.sort.ListingSortStrategy;
import com.carousell.marketplace.command.sort.SortStrategyFactory;
import com.carousell.marketplace.model.Listing;
import com.carousell.marketplace.repository.MarketplaceReader;
import com.carousell.marketplace.util.Parser;
import java.util.List;
import java.util.stream.Collectors;

public class GetCategoryCommand implements Command {
    private final MarketplaceReader reader;

    public GetCategoryCommand(MarketplaceReader reader) {
        this.reader = reader;
    }

    @Override
    public String execute(String[] args) {
        if (args.length < 5) return "Error - invalid arguments";

        if (!reader.userExists(args[1])) return "Error - unknown user";

        String category = Parser.stripQuotes(args[2]);
        List<Listing> filtered = reader.getAllListings().stream()
                .filter(l -> l.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());

        if (filtered.isEmpty()) return "Error - category not found"; // Requirement: Silence for empty category

        // Strategy Pattern for sorting
        ListingSortStrategy strategy = SortStrategyFactory.getStrategy(args[3], args[4]);
        filtered.sort(strategy.getComparator());

        return filtered.stream().map(this::format).collect(Collectors.joining("\n"));
    }

    private String format(Listing l) {
        return String.format("%s|%s|%.0f|%s|%s|%s",
                l.getTitle(), l.getDescription(), l.getPrice(),
                l.getCreatedAt(), l.getCategory(), l.getOwner());
    }
}