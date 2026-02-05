package com.carousell.marketplace.command.sort;

import com.carousell.marketplace.model.Listing;

import java.util.Comparator;

public class PriceSortStrategy implements ListingSortStrategy {
    private final String order;

    public PriceSortStrategy(String order) { this.order = order; }

    @Override
    public Comparator<Listing> getComparator() {
        Comparator<Listing> comp = Comparator.comparingDouble(Listing::getPrice);
        return "dsc".equalsIgnoreCase(order) ? comp.reversed() : comp;
    }
}
