package com.carousell.marketplace;

import com.carousell.marketplace.repository.MarketplaceRepository;
import com.carousell.marketplace.model.Listing;

/**
 * Unit test suite using Java standard assertions.
 * Run with: java -ea -cp out com.carousell.marketplace.MarketplaceTest
 */
public class MarketplaceTest {

    public static void main(String[] args) {
        MarketplaceTest tests = new MarketplaceTest();

        System.out.println("Starting Marketplace Unit Tests...");

        tests.testUserRegistration();
        tests.testListingCreationAndValidation();
        tests.testTopCategoryLogicWithTies();
        tests.testDeleteAndTopCategoryShift();

        System.out.println("All tests passed successfully!");
    }

    /**
     * Tests registration and addresses the "race condition" feedback
     * by ensuring duplicate registration returns false.
     */
    void testUserRegistration() {
        MarketplaceRepository repo = new MarketplaceRepository();

        assert repo.registerUser("user1") : "Should register new user";
        assert !repo.registerUser("USER1") : "Should fail on duplicate case-insensitive user";
        assert repo.userExists("user1") : "User should exist";
    }

    /**
     * Tests listing creation and verifies that the repository correctly
     * stores the data provided by the command layer.
     */
    void testListingCreationAndValidation() {
        MarketplaceRepository repo = new MarketplaceRepository();
        repo.registerUser("user1");

        int id = repo.addListing("user1", "Phone", "New", 100.0, "Electronics", "2024-01-01 10:00:00");
        Listing l = repo.getListing(id);

        assert l != null : "Listing should be retrieved";
        assert "Electronics".equals(l.getCategory()) : "Category should match";
        assert l.getPrice() == 100.0 : "Price should match";
    }

    /**
     * Tests the O(1) Top Category logic and tie-breaking (Incumbent stability).
     */
    void testTopCategoryLogicWithTies() {
        MarketplaceRepository repo = new MarketplaceRepository();
        repo.registerUser("user1");

        // Add 2 to Sports, 2 to Electronics
        repo.addListing("user1", "S1", "Desc", 10.0, "Sports", "time");
        repo.addListing("user1", "S2", "Desc", 10.0, "Sports", "time");

        assert "Sports".equalsIgnoreCase(repo.getCachedTopCategory()) : "Sports should be top";

        repo.addListing("user1", "E1", "Desc", 10.0, "Electronics", "time");
        repo.addListing("user1", "E2", "Desc", 10.0, "Electronics", "time");

        // Tie-breaker: Sports was there first, so it stays incumbent
        assert "Sports".equalsIgnoreCase(repo.getCachedTopCategory()) : "Sports should remain top during tie";

        // Add 1 more to Electronics to make it strictly greater
        repo.addListing("user1", "E3", "Desc", 10.0, "Electronics", "time");
        assert "Electronics".equalsIgnoreCase(repo.getCachedTopCategory()) : "Electronics should take lead";
    }

    /**
     * Tests that deleting a listing correctly triggers a re-evaluation of the top category.
     */
    void testDeleteAndTopCategoryShift() {
        MarketplaceRepository repo = new MarketplaceRepository();
        repo.registerUser("user1");

        int s1 = repo.addListing("user1", "S1", "Desc", 10.0, "Sports", "time");
        repo.addListing("user1", "E1", "Desc", 10.0, "Electronics", "time");

        // Delete the only Sports listing
        repo.deleteListing(s1);

        assert "Electronics".equalsIgnoreCase(repo.getCachedTopCategory()) : "Top category should shift to Electronics";
    }
}