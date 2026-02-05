package com.carousell.marketplace.command;

public interface Command {
    /**
     * Executes the marketplace command logic.
     * @param args The split input parts from the CLI.
     * @return The response string to be sent to STDOUT.
     */
    String execute(String[] args);
}