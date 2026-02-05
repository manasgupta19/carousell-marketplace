package com.carousell.marketplace.command;

import com.carousell.marketplace.model.Listing;
import com.carousell.marketplace.repository.MarketplaceReader;
import com.carousell.marketplace.repository.MarketplaceWriter;

public class DeleteListingCommand implements Command {
    private final MarketplaceReader reader;
    private final MarketplaceWriter writer;

    public DeleteListingCommand(MarketplaceReader reader, MarketplaceWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    @Override
    public String execute(String[] args) {
        if (args.length < 3) return "Error - invalid arguments";

        String user = args[1];
        if (!reader.userExists(user)) return "Error - unknown user";

        int id = Integer.parseInt(args[2]);
        Listing listing = reader.getListing(id);

        if (listing == null) return "Error - listing not found";
        if (!listing.getOwner().equalsIgnoreCase(user)) return "Error - listing owner mismatch";

        writer.deleteListing(id);
        return "Success";
    }
}