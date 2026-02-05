package com.carousell.marketplace.command;

import com.carousell.marketplace.model.Listing;
import com.carousell.marketplace.repository.MarketplaceReader;

/**
 * Handles the retrieval of a specific listing.
 * Strictly adheres to the pipe-separated output format defined in the specification.
 */
public class GetListingCommand implements Command {
    private final MarketplaceReader reader;

    public GetListingCommand(MarketplaceReader reader) {
        this.reader = reader;
    }

    @Override
    public String execute(String[] args) {
        // Format: GET_LISTING <user> <listing_id>
        if (args.length < 3) return "Error - invalid arguments";

        String username = args[1];
        if (!reader.userExists(username)) return "Error - unknown user";

        try {
            int listingId = Integer.parseInt(args[2]);
            Listing listing = reader.getListing(listingId);

            if (listing == null) return "Error - not found";

            // Output Order: title|description|price|timestamp|category|user
            // Price is formatted as %.0f to match "1000" instead of "1000.0"
            return String.format("%s|%s|%.0f|%s|%s|%s",
                    listing.getTitle(),
                    listing.getDescription(),
                    listing.getPrice(),
                    listing.getCreatedAt(),
                    listing.getCategory(),
                    listing.getOwner());

        } catch (NumberFormatException e) {
            return "Error - invalid listing id";
        }
    }
}