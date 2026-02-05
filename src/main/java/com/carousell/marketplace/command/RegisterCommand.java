package com.carousell.marketplace.command;

import com.carousell.marketplace.repository.MarketplaceReader;
import com.carousell.marketplace.repository.MarketplaceWriter;

public class RegisterCommand implements Command {
    private final MarketplaceReader reader;
    private final MarketplaceWriter writer;

    public RegisterCommand(MarketplaceReader reader, MarketplaceWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    @Override
    public String execute(String[] args) {
        if (args.length < 2) return "Error - invalid arguments";
        String username = args[1];
        if (reader.userExists(username)) return "Error - user already existing";

        writer.registerUser(username);
        return "Success";
    }
}