package com.carousell.marketplace.repository;

import com.carousell.marketplace.model.Category;
import com.carousell.marketplace.model.Listing;
import com.carousell.marketplace.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * MarketplaceRepository acts as the in-memory data store.
 * Updated to fix race conditions, handle Category entities, and optimize concurrency.
 */
public class MarketplaceRepository implements MarketplaceReader, MarketplaceWriter {
    // Thread-safe storage for users and listings
    private final Map<String, User> users = new ConcurrentHashMap<>();
    private final Map<Integer, Listing> listings = new ConcurrentHashMap<>();

    // Entity-based category storage to avoid "stringly-typed" logic
    private final Map<String, Category> categories = new ConcurrentHashMap<>();

    // Sequential ID generator starting at 100001
    private final AtomicInteger idGenerator = new AtomicInteger(100001);

    // Optimization: Category frequency tracking for O(1) Top Category retrieval
    private final Map<String, Integer> categoryCounts = new ConcurrentHashMap<>();
    private volatile String cachedTopCategory = "";

    /**
     * Fixes the race condition using putIfAbsent.
     * Returns true if registration was successful, false if user already exists.
     */
    @Override
    public boolean registerUser(String username) {
        String normalized = username.toLowerCase();
        return users.putIfAbsent(normalized, new User(username)) == null;
    }

    @Override
    public boolean userExists(String username) {
        if (username == null) return false;
        return users.containsKey(username.toLowerCase());
    }

    /**
     * Adds a listing and updates the cached top category.
     * Uses Category entity instead of plain String.
     */
    @Override
    public int addListing(String owner, String title, String desc, double price, String categoryName, String timestamp) {
        int id = idGenerator.getAndIncrement();

        // Ensure Category entity exists
        categories.putIfAbsent(categoryName.toLowerCase(), new Category(categoryName));

        Listing listing = new Listing(id, title, desc, price, timestamp, categoryName, owner);
        listings.put(id, listing);

        updateCategoryRanking(categoryName, 1);
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
     * Synchronized only on the ranking logic to prevent race conditions on the leader pointer.
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

        // Case 1: A category attains a count STRICTLY GREATER than the current leader
        if (newCount > currentTopCount) {
            this.cachedTopCategory = formatCategoryDisplay(normalized);
        }
        // Case 2: The current leader's count decreased
        else if (normalized.equals(currentTopNormalized) && delta < 0) {
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
            // Tie-breaker: keep incumbent to satisfy "first-in" stability
        }

        this.cachedTopCategory = maxCount > 0 ? formatCategoryDisplay(bestCategory) : "";
    }

    private String formatCategoryDisplay(String cat) {
        if (cat == null || cat.isEmpty()) return "";
        // Retrieve original casing from Category entity if available, else capitalize
        Category entity = categories.get(cat.toLowerCase());
        return (entity != null) ? entity.getName() : cat.substring(0, 1).toUpperCase() + cat.substring(1);
    }
}