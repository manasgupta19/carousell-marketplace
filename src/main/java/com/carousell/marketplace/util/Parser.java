package com.carousell.marketplace.util;

public class Parser {
    // Standard library regex for handling quoted strings in CLI
    private static final String SPLIT_REGEX = " +(?=(?:[^']*'[^']*')*[^']*$)";

    public static String[] parse(String input) {
        return input.trim().split(SPLIT_REGEX);
    }

    public static String stripQuotes(String input) {
        return input.replaceAll("^'|'$", "");
    }
}