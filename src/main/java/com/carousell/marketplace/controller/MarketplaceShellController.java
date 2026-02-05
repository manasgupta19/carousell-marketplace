package com.carousell.marketplace.controller;

import com.carousell.marketplace.command.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.Map;

/**
 * Controller that exposes the marketplace logic as CLI commands.
 * Leverages Spring Shell to handle the STDIN/STDOUT loop automatically.
 */
@ShellComponent
public class MarketplaceShellController {

    // Spring injects all beans implementing the Command interface into this map.
    // The key is the bean name (e.g., "REGISTER", "CREATE_LISTING").
    @Autowired
    private Map<String, Command> commands;

    @ShellMethod(key = "REGISTER", value = "Register a new user")
    public String register(String username) {
        return execute("REGISTER", username);
    }

    @ShellMethod(key = "CREATE_LISTING", value = "Create a new listing")
    public String createListing(
            String username,
            String title,
            String description,
            double price,
            String category) {
        return execute("CREATE_LISTING", username, title, description, String.valueOf(price), category);
    }

    @ShellMethod(key = "GET_LISTING", value = "Retrieve a listing by ID")
    public String getListing(String username, String listingId) {
        return execute("GET_LISTING", username, listingId);
    }

    @ShellMethod(key = "DELETE_LISTING", value = "Delete a listing")
    public String deleteListing(String username, String listingId) {
        return execute("DELETE_LISTING", username, listingId);
    }

    @ShellMethod(key = "GET_CATEGORY", value = "Get listings in a category with sorting")
    public String getCategory(
            String username,
            String category,
            String sortKey,
            String sortOrder) {
        return execute("GET_CATEGORY", username, category, sortKey, sortOrder);
    }

    @ShellMethod(key = "GET_TOP_CATEGORY", value = "Get the highest volume category")
    public String getTopCategory(String username) {
        return execute("GET_TOP_CATEGORY", username);
    }

    /**
     * Helper to bridge the Shell input to the Command pattern logic.
     */
    private String execute(String cmdName, String... args) {
        Command cmd = commands.get(cmdName);
        if (cmd == null) {
            return "Error - unknown command";
        }

        // Reconstruct the args array for the Command.execute method
        String[] fullArgs = new String[args.length + 1];
        fullArgs[0] = cmdName;
        System.arraycopy(args, 0, fullArgs, 1, args.length);

        return cmd.execute(fullArgs);
    }
}