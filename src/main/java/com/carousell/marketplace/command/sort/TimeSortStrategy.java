package com.carousell.marketplace.command.sort;

import com.carousell.marketplace.model.Listing;
import lombok.RequiredArgsConstructor;
import java.util.Comparator;

@RequiredArgsConstructor
class TimeSortStrategy implements ListingSortStrategy {
    private final String order;

    @Override
    public Comparator<Listing> getComparator() {
        Comparator<Listing> comp = Comparator.comparing(Listing::getCreatedAt);
        return "dsc".equalsIgnoreCase(order) ? comp.reversed() : comp;
    }
}