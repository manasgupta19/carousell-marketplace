package com.carousell.marketplace.repository;

import com.carousell.marketplace.model.Listing;
import com.carousell.marketplace.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * MarketplaceRepository acts as the in-memory data store.
 * Optimized for GET_TOP_CATEGORY as a read-heavy operation.
 */
public class MarketplaceRepository implements MarketplaceReader, MarketplaceWriter {
    // Thread-safe storage for users and listings
    private final Map<String, User> users = new ConcurrentHashMap<>();
    private final Map<Integer, Listing> listings = new ConcurrentHashMap<>();

    // Sequential ID generator starting at 100001
    private final AtomicInteger idGenerator = new AtomicInteger(100001);

    // Optimization: Category frequency tracking for O(1) Top Category retrieval
    private final Map<String, Integer> categoryCounts = new ConcurrentHashMap<>();
    private volatile String cachedTopCategory = "";

    /**
     * Registers a user. Usernames are treated as case-insensitive.
     */
    @Override
    public void registerUser(String username) {
        users.put(username.toLowerCase(), new User(username));
    }

    /**
     * Case-insensitive check for user existence.
     */
    @Override
    public boolean userExists(String username) {
        if (username == null) return false;
        return users.containsKey(username.toLowerCase());
    }

    /**
     * Adds a listing and updates the cached top category.
     * Complexity: O(N) where N is the number of unique categories (very small).
     */
    @Override
    public int addListing(String owner, String title, String desc, double price, String category, String timestamp) {
        int id = idGenerator.getAndIncrement();
        Listing listing = new Listing(id, title, desc, price, timestamp, category, owner);

        listings.put(id, listing);
        updateCategoryRanking(category, 1);

        return id;
    }

    /**
     * Retrieves a listing by ID.
     */
    @Override
    public Listing getListing(int id) {
        return listings.get(id);
    }

    /**
     * Removes a listing and updates the cached top category.
     */
    @Override
    public void deleteListing(int id) {
        Listing removed = listings.remove(id);
        if (removed != null) {
            updateCategoryRanking(removed.getCategory(), -1);
        }
    }

    /**
     * Optimized Read: Returns the pre-calculated top category in O(1).
     */
    @Override
    public String getCachedTopCategory() {
        return cachedTopCategory;
    }

    /**
     * Returns all listings for filtering/sorting.
     */
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

        // Initial case: No leader exists yet
        if (this.cachedTopCategory.isEmpty()) {
            if (newCount > 0) {
                this.cachedTopCategory = formatCategoryDisplay(normalized);
            }
            return;
        }

        String currentTopNormalized = this.cachedTopCategory.toLowerCase();
        int currentTopCount = categoryCounts.getOrDefault(currentTopNormalized, 0);

        // Case 1: A category attains a count STRICTLY GREATER than the current leader
        if (newCount > currentTopCount) {
            this.cachedTopCategory = formatCategoryDisplay(normalized);
        }
        // Case 2: The current leader's count decreased (due to a deletion)
        else if (normalized.equals(currentTopNormalized) && delta < 0) {
            reEvaluateIncumbent(currentTopNormalized, newCount);
        }
    }

    private void reEvaluateIncumbent(String incumbent, int incumbentCount) {
        String bestCategory = incumbent;
        int maxCount = incumbentCount;

        for (Map.Entry<String, Integer> entry : categoryCounts.entrySet()) {
            int count = entry.getValue();
            String cat = entry.getKey();

            // Only switch if another category is strictly better than the incumbent
            if (count > maxCount) {
                maxCount = count;
                bestCategory = cat;
            }
            // If counts are equal, we do nothing to preserve the incumbent (Sports stays leader)
        }

        this.cachedTopCategory = maxCount > 0 ? formatCategoryDisplay(bestCategory) : "";
    }

    /**
     * Recalculates the top category.
     * Handles ties by picking the alphabetically smaller category.
     */
    private void refreshTopCategoryCache() {
        String top = "";
        int max = 0;

        for (Map.Entry<String, Integer> entry : categoryCounts.entrySet()) {
            int count = entry.getValue();
            if (count > max) {
                max = count;
                top = entry.getKey();
            }
            // If count == max, we do nothing (keep the existing 'top' to satisfy 'first-in')
        }
        this.cachedTopCategory = formatCategoryDisplay(top);
    }

    private String formatCategoryDisplay(String cat) {
        if (cat == null || cat.isEmpty()) return "";
        return cat.substring(0, 1).toUpperCase() + cat.substring(1);
    }
}