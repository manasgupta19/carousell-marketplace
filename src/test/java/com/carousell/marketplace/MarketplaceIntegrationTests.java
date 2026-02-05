package com.carousell.marketplace;

import com.carousell.marketplace.command.Command;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for the Marketplace.
 * Properties are set to disable the interactive shell to prevent the build from hanging.
 */
@SpringBootTest(properties = {
        "spring.shell.interactive.enabled=false",
        "spring.shell.command.script.enabled=false"
})
class MarketplaceIntegrationTests {

    @Autowired
    private Map<String, Command> commands;

    @Test
    @DisplayName("Should register a new user successfully")
    void testRegistration() {
        Command register = commands.get("REGISTER");
        String result = register.execute(new String[]{"REGISTER", "testuser"});
        assertThat(result).isEqualTo("Success");
    }

    @Test
    @DisplayName("Should maintain top category incumbent on ties")
    void testTopCategoryTie() {
        Command create = commands.get("CREATE_LISTING");
        Command topCat = commands.get("GET_TOP_CATEGORY");

        commands.get("REGISTER").execute(new String[]{"REGISTER", "user1"});

        // Sports: 1
        create.execute(new String[]{"CREATE_LISTING", "user1", "S1", "Desc", "10", "Sports"});
        assertThat(topCat.execute(new String[]{"GET_TOP_CATEGORY", "user1"})).isEqualTo("Sports");

        // Electronics: 1 (Tie)
        create.execute(new String[]{"CREATE_LISTING", "user1", "E1", "Desc", "20", "Electronics"});

        // Sports was first, it should stay as the top category
        assertThat(topCat.execute(new String[]{"GET_TOP_CATEGORY", "user1"})).isEqualTo("Sports");
    }
}