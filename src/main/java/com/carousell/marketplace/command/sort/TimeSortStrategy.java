package com.carousell.marketplace.command.sort;

import com.carousell.marketplace.model.Listing;

import java.util.Comparator;

// Strategy for Time Sorting
class TimeSortStrategy implements ListingSortStrategy {
    private final String order;
    public TimeSortStrategy(String order) { this.order = order; }

    @Override
    public Comparator<Listing> getComparator() {
        Comparator<Listing> comp = Comparator.comparing(Listing::getCreatedAt);
        return "dsc".equalsIgnoreCase(order) ? comp.reversed() : comp;
    }
}
