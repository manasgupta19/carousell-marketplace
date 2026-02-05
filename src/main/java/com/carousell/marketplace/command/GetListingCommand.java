package com.carousell.marketplace.command;

import com.carousell.marketplace.model.Listing;
import com.carousell.marketplace.repository.MarketplaceReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Spring-managed component for retrieving specific listings.
 * Strictly adheres to the required pipe-separated output format.
 */
@Component("GET_LISTING")
@RequiredArgsConstructor
public class GetListingCommand implements Command {

    private final MarketplaceReader reader;

    @Override
    public String execute(String[] args) {
        if (args.length < 3) return "Error - invalid arguments";

        String username = args[1];
        if (!reader.userExists(username)) return "Error - unknown user";

        try {
            int listingId = Integer.parseInt(args[2]);
            Listing listing = reader.getListing(listingId);

            if (listing == null) return "Error - not found";

            // Format: title|description|price|created_at|category|username
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