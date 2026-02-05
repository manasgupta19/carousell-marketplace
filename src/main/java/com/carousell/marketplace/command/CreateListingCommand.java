package com.carousell.marketplace.command;

import com.carousell.marketplace.repository.MarketplaceReader;
import com.carousell.marketplace.repository.MarketplaceWriter;
import com.carousell.marketplace.util.Parser;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Spring-managed component for creating marketplace listings.
 * Implements strict business validation and dynamic timestamps.
 */
@Component("CREATE_LISTING")
public class CreateListingCommand implements Command {

    private final MarketplaceReader reader;
    private final MarketplaceWriter writer;

    // Using modern java.time API for thread-safe date formatting
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Spring automatically injects the singleton MarketplaceRepository bean here.
     */
    public CreateListingCommand(MarketplaceReader reader, MarketplaceWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    @Override
    public String execute(String[] args) {
        // 1. Argument validation
        if (args.length < 6) {
            return "Error - invalid arguments";
        }

        // 2. Authentication check
        String username = args[1];
        if (!reader.userExists(username)) {
            return "Error - unknown user";
        }

        // 3. Data extraction and quote stripping
        String title = Parser.stripQuotes(args[2]);
        String desc = Parser.stripQuotes(args[3]);
        String category = Parser.stripQuotes(args[5]);

        // 4. Business Validation (Addressing rejection feedback)
        if (isInvalid(title) || isInvalid(desc) || isInvalid(category)) {
            return "Error - title, description, and category cannot be empty";
        }

        double price;
        try {
            price = Double.parseDouble(args[4]);
            if (price <= 0) {
                return "Error - price must be a positive value";
            }
        } catch (NumberFormatException e) {
            return "Error - invalid price format";
        }

        // 5. Dynamic Timestamp (Addressing rejection feedback)
        String timestamp = LocalDateTime.now().format(FORMATTER);

        // 6. Persistence
        int id = writer.addListing(username, title, desc, price, category, timestamp);
        return String.valueOf(id);
    }

    private boolean isInvalid(String str) {
        return str == null || str.trim().isEmpty();
    }
}