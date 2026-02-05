package com.carousell.marketplace.command;

import com.carousell.marketplace.repository.MarketplaceReader;
import com.carousell.marketplace.repository.MarketplaceWriter;
import com.carousell.marketplace.util.Parser;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Handles the creation of new listings.
 * Updated to include strict input validation and dynamic timestamps.
 */
public class CreateListingCommand implements Command {
    private final MarketplaceReader reader;
    private final MarketplaceWriter writer;

    // Requirements baseline format
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public CreateListingCommand(MarketplaceReader reader, MarketplaceWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    @Override
    public String execute(String[] args) {
        // 1. Basic Argument Length Check
        if (args.length < 6) return "Error - invalid arguments";

        // 2. User Authentication Check
        String username = args[1];
        if (!reader.userExists(username)) return "Error - unknown user";

        // 3. Input Extraction and Sanitization
        String title = Parser.stripQuotes(args[2]);
        String desc = Parser.stripQuotes(args[3]);
        String category = Parser.stripQuotes(args[5]);

        // 4. Input Validation (Addressing Hiring Team Feedback)
        if (isInvalidString(title) || isInvalidString(desc) || isInvalidString(category)) {
            return "Error - empty title, description, or category";
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

        // 5. Dynamic Timestamp Generation
        // In a production environment, this would be injected via a Clock or Supplier
        String timestamp = new SimpleDateFormat(DATE_FORMAT).format(new Date());

        // 6. Persistence
        int id = writer.addListing(username, title, desc, price, category, timestamp);
        return String.valueOf(id);
    }

    /**
     * Helper to validate that strings are not null, empty, or just whitespace.
     */
    private boolean isInvalidString(String str) {
        return str == null || str.trim().isEmpty();
    }
}