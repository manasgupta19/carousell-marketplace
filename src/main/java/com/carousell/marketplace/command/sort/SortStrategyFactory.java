package com.carousell.marketplace.command.sort;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SortStrategyFactory {
    public static ListingSortStrategy getStrategy(String sortKey, String sortOrder) {
        if ("sort_price".equalsIgnoreCase(sortKey)) {
            return new PriceSortStrategy(sortOrder);
        }
        // Default to Time Sorting as per requirements
        return new TimeSortStrategy(sortOrder);
    }
}