package com.carousell.marketplace.command.sort;

import com.carousell.marketplace.model.Listing;
import lombok.RequiredArgsConstructor;
import java.util.Comparator;

@RequiredArgsConstructor
public class PriceSortStrategy implements ListingSortStrategy {
    private final String order;

    @Override
    public Comparator<Listing> getComparator() {
        Comparator<Listing> comp = Comparator.comparingDouble(Listing::getPrice);
        return "dsc".equalsIgnoreCase(order) ? comp.reversed() : comp;
    }
}