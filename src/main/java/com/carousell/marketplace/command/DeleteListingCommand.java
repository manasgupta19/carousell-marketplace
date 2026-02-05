package com.carousell.marketplace.command;

import com.carousell.marketplace.model.Listing;
import com.carousell.marketplace.repository.MarketplaceReader;
import com.carousell.marketplace.repository.MarketplaceWriter;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

/**
 * Spring-managed component for deleting listings.
 * Ensures strict ownership checks before removal.
 */
@Component("DELETE_LISTING")
@RequiredArgsConstructor
public class DeleteListingCommand implements Command {
    private final MarketplaceReader reader;
    private final MarketplaceWriter writer;

    @Override
    public String execute(String[] args) {
        if (args.length < 3) return "Error - invalid arguments";

        String user = args[1];
        if (!reader.userExists(user)) return "Error - unknown user";

        try {
            int id = Integer.parseInt(args[2]);
            Listing listing = reader.getListing(id);

            if (listing == null) return "Error - listing does not exist";

            // Validation: Only the owner can delete their listing
            if (!listing.getOwner().equalsIgnoreCase(user)) {
                return "Error - listing owner mismatch";
            }

            writer.deleteListing(id);
            return "Success";

        } catch (NumberFormatException e) {
            return "Error - invalid listing id";
        }
    }
}