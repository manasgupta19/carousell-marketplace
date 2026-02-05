package com.carousell.marketplace;

import com.carousell.marketplace.command.Command;
import com.carousell.marketplace.command.CommandFactory;
import com.carousell.marketplace.repository.MarketplaceRepository;
import com.carousell.marketplace.util.Parser;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        MarketplaceRepository repository = new MarketplaceRepository();
        CommandFactory factory = new CommandFactory(repository);
        Scanner scanner = new Scanner(System.in);

        System.out.print("# ");

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            String[] parts = Parser.parse(input);

            if (parts.length > 0) {
                Command command = factory.getCommand(parts[0]);
                if (command != null) {
                    String result = command.execute(parts);
                    if (result != null && !result.isEmpty()) {
                        System.out.println(result);
                    }
                } else {
                    System.out.println("Error - unknown command");
                }
            }
            System.out.print("# ");
        }
    }
}