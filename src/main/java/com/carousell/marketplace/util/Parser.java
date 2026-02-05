package com.carousell.marketplace.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Parser {
    // Regex to split by space while preserving content within single quotes
    private static final String SPLIT_REGEX = " +(?=(?:[^']*'[^']*')*[^']*$)";

    public static String[] parse(String input) {
        if (input == null) return new String[0];
        return input.trim().split(SPLIT_REGEX);
    }

    public static String stripQuotes(String input) {
        if (input == null) return "";
        return input.replaceAll("^'|'$", "");
    }
}