package com.carousell.marketplace;

import com.carousell.marketplace.command.Command;
import com.carousell.marketplace.command.CommandFactory;
import com.carousell.marketplace.repository.MarketplaceRepository;
import com.carousell.marketplace.util.Parser;

import java.util.Scanner;

/**
 * Entry point for the Marketplace CLI.
 * Updated to properly manage system resources and handle input streams.
 */
public class Main {
    public static void main(String[] args) {
        MarketplaceRepository repository = new MarketplaceRepository();
        CommandFactory factory = new CommandFactory(repository);

        // Try-with-resources ensures the Scanner is closed even if an exception occurs
        try (Scanner scanner = new Scanner(System.in)) {
            // Display initial prompt
            if (System.console() != null) {
                System.out.print("# ");
            }

            while (scanner.hasNextLine()) {
                String input = scanner.nextLine();

                // Skip empty lines to prevent unnecessary processing
                if (input == null || input.trim().isEmpty()) {
                    continue;
                }

                String[] parts = Parser.parse(input);

                if (parts.length > 0) {
                    Command command = factory.getCommand(parts[0]);
                    if (command != null) {
                        try {
                            String result = command.execute(parts);
                            if (result != null && !result.isEmpty()) {
                                System.out.println(result);
                            }
                        } catch (Exception e) {
                            // Defensive catch-all for unexpected command failures
                            System.out.println("Error - internal command error");
                        }
                    } else {
                        System.out.println("Error - unknown command");
                    }
                }

                // Display prompt for the next input
                if (System.console() != null) {
                    System.out.print("# ");
                }
            }
        }
    }
}