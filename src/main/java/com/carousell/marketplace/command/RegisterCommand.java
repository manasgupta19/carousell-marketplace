package com.carousell.marketplace.command;

import com.carousell.marketplace.repository.MarketplaceReader;
import com.carousell.marketplace.repository.MarketplaceWriter;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

/**
 * Spring-managed component for user registration.
 * Uses Lombok @RequiredArgsConstructor for clean constructor injection.
 */
@Component("REGISTER")
@RequiredArgsConstructor
public class RegisterCommand implements Command {
    private final MarketplaceReader reader;
    private final MarketplaceWriter writer;

    @Override
    public String execute(String[] args) {
        if (args.length < 2) return "Error - invalid arguments";

        String username = args[1];

        // Atomically attempt registration to prevent race conditions
        boolean success = writer.registerUser(username);

        return success ? "Success" : "Error - user already existing";
    }
}