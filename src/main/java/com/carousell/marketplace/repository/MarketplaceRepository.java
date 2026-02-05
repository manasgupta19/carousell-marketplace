package com.carousell.marketplace.repository;

import com.carousell.marketplace.model.Category;
import com.carousell.marketplace.model.Listing;
import com.carousell.marketplace.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Spring-managed repository for in-memory data storage.
 * Optimized for O(1) retrieval of the top category and thread-safe operations.
 */
@Slf4j
@Repository
public class MarketplaceRepository implements MarketplaceReader, MarketplaceWriter {

    private final Map<String, User> users = new ConcurrentHashMap<>();
    private final Map<Integer, Listing> listings = new ConcurrentHashMap<>();
    private final Map<String, Category> categories = new ConcurrentHashMap<>();

    private final AtomicInteger idGenerator = new AtomicInteger(100001);

    // Optimization: Category frequency tracking for O(1) Top Category retrieval
    private final Map<String, Integer> categoryCounts = new ConcurrentHashMap<>();
    private volatile String cachedTopCategory = "";

    /**
     * Fixes race conditions using putIfAbsent.
     * @return true if registration was successful.
     */
    @Override
    public boolean registerUser(String username) {
        String normalized = username.toLowerCase();
        return users.putIfAbsent(normalized, new User(username)) == null;
    }

    @Override
    public boolean userExists(String username) {
        return username != null && users.containsKey(username.toLowerCase());
    }

    /**
     * Adds a listing and updates the cached top category.
     */
    @Override
    public int addListing(String owner, String title, String desc, double price, String categoryName, String timestamp) {
        int id = idGenerator.getAndIncrement();

        // Ensure Category entity exists to avoid "stringly-typed" logic
        categories.putIfAbsent(categoryName.toLowerCase(), new Category(categoryName));

        Listing listing = Listing.builder()
                .id(id)
                .title(title)
                .description(desc)
                .price(price)
                .createdAt(timestamp)
                .category(categoryName)
                .owner(owner)
                .build();

        listings.put(id, listing);
        updateCategoryRanking(categoryName, 1);

        log.info("Created listing {} for user {}", id, owner);
        return id;
    }

    @Override
    public Listing getListing(int id) {
        return listings.get(id);
    }

    @Override
    public void deleteListing(int id) {
        Listing removed = listings.remove(id);
        if (removed != null) {
            updateCategoryRanking(removed.getCategory(), -1);
        }
    }

    @Override
    public String getCachedTopCategory() {
        return cachedTopCategory;
    }

    @Override
    public List<Listing> getAllListings() {
        return new ArrayList<>(listings.values());
    }

    /**
     * Internal logic to maintain the cached top category during writes.
     * Synchronized to prevent race conditions during the refresh of the top value.
     */
    private synchronized void updateCategoryRanking(String category, int delta) {
        String normalized = category.toLowerCase();
        int newCount = categoryCounts.merge(normalized, delta, Integer::sum);

        if (this.cachedTopCategory.isEmpty()) {
            if (newCount > 0) {
                this.cachedTopCategory = formatCategoryDisplay(normalized);
            }
            return;
        }

        String currentTopNormalized = this.cachedTopCategory.toLowerCase();
        int currentTopCount = categoryCounts.getOrDefault(currentTopNormalized, 0);

        // Tie-breaking: A leader is only replaced if a challenger's count is strictly greater
        if (newCount > currentTopCount) {
            this.cachedTopCategory = formatCategoryDisplay(normalized);
        } else if (normalized.equals(currentTopNormalized) && delta < 0) {
            reEvaluateIncumbent(currentTopNormalized, newCount);
        }
    }

    private void reEvaluateIncumbent(String incumbent, int incumbentCount) {
        String bestCategory = incumbent;
        int maxCount = incumbentCount;

        for (Map.Entry<String, Integer> entry : categoryCounts.entrySet()) {
            int count = entry.getValue();
            if (count > maxCount) {
                maxCount = count;
                bestCategory = entry.getKey();
            }
        }

        this.cachedTopCategory = maxCount > 0 ? formatCategoryDisplay(bestCategory) : "";
    }

    private String formatCategoryDisplay(String cat) {
        if (cat == null || cat.isEmpty()) return "";
        Category entity = categories.get(cat.toLowerCase());
        return (entity != null) ? entity.getName() : cat;
    }
}