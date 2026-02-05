package com.carousell.marketplace.command.sort;

public class SortStrategyFactory {
    public static ListingSortStrategy getStrategy(String sortKey, String sortOrder) {
        switch (sortKey.toLowerCase()) {
            case "sort_price":
                return new PriceSortStrategy(sortOrder);
            case "sort_time":
            default:
                return new TimeSortStrategy(sortOrder);
        }
    }
}