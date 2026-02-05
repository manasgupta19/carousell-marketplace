package com.carousell.marketplace.command;

import com.carousell.marketplace.repository.MarketplaceRepository;
import java.util.HashMap;
import java.util.Map;

/**
 * The CommandFactory manages the registry of available CLI operations.
 * It facilitates Dependency Injection by providing the Repository's functional
 * interfaces to the commands at instantiation.
 */
public class CommandFactory {
    private final Map<String, Command> registry = new HashMap<>();

    public CommandFactory(MarketplaceRepository repository) {
        // Registering commands with injected dependencies.
        // MarketplaceRepository is passed as both Reader and Writer where required.
        registry.put("REGISTER", new RegisterCommand(repository, repository));
        registry.put("CREATE_LISTING", new CreateListingCommand(repository, repository));
        registry.put("GET_LISTING", new GetListingCommand(repository));
        registry.put("GET_CATEGORY", new GetCategoryCommand(repository));
        registry.put("GET_TOP_CATEGORY", new GetTopCategoryCommand(repository));
        registry.put("DELETE_LISTING", new DeleteListingCommand(repository, repository));
    }

    /**
     * Retrieves the command associated with the given input string.
     * @param name The command keyword (e.g., "REGISTER")
     * @return The corresponding Command implementation, or null if not found.
     */
    public Command getCommand(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        return registry.get(name.toUpperCase().trim());
    }
}