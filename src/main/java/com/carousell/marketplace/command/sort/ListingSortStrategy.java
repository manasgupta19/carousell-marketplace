package com.carousell.marketplace.command.sort;

import com.carousell.marketplace.model.Listing;
import java.util.Comparator;

public interface ListingSortStrategy {
    Comparator<Listing> getComparator();
}