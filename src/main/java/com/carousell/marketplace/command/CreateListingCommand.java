package com.carousell.marketplace.command;

import com.carousell.marketplace.repository.MarketplaceReader;
import com.carousell.marketplace.repository.MarketplaceWriter;
import com.carousell.marketplace.util.Parser;

public class CreateListingCommand implements Command {
    private final MarketplaceReader reader;
    private final MarketplaceWriter writer;

    public CreateListingCommand(MarketplaceReader reader, MarketplaceWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    @Override
    public String execute(String[] args) {
        if (args.length < 6) return "Error - invalid arguments";

        String username = args[1];
        if (!reader.userExists(username)) return "Error - unknown user";

        String title = Parser.stripQuotes(args[2]);
        String desc = Parser.stripQuotes(args[3]);
        double price = Double.parseDouble(args[4]);
        String category = Parser.stripQuotes(args[5]);
        String timestamp = "2019-02-22 12:34:56"; // Requirement baseline

        int id = writer.addListing(username, title, desc, price, category, timestamp);
        return String.valueOf(id);
    }
}