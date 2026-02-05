package com.carousell.marketplace.command.sort;

import com.carousell.marketplace.model.Listing;
import java.util.Comparator;

/**
 * Functional interface for sorting strategies.
 */
public interface ListingSortStrategy {
    Comparator<Listing> getComparator();
}