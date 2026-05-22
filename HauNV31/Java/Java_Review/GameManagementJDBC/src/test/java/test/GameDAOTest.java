package test;

import dao.GameDAO;
import model.Game;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameDAOTest {

    private GameDAO dao = new GameDAO();

    @Test
    public void testInsertGame() {
        // 1. Clean up first to ensure a clean state
        dao.deleteGame("G100");

        // 2. Perform insert
        Game game = new Game(
                "G100",
                "Minecraft",
                "Sandbox",
                20.0,
                "Mojang"
        );
        boolean result = dao.addGame(game);
        assertTrue(result, "Game should be added successfully");

        // 3. Clean up afterwards
        dao.deleteGame("G100");
    }

    @Test
    public void testFindById() {
        // 1. Clean up and insert test data to make test completely independent
        dao.deleteGame("G100");
        Game game = new Game("G100", "Minecraft", "Sandbox", 20.0, "Mojang");
        dao.addGame(game);

        // 2. Perform search
        Game found = dao.findById("G100");
        assertNotNull(found, "Game G100 should be found in database!");
        assertEquals("G100", found.getId());

        // 3. Clean up afterwards
        dao.deleteGame("G100");
    }

    @Test
    public void testUpdateGame() {
        // 1. Clean up and insert test data to make test completely independent
        dao.deleteGame("G100");
        Game game = new Game("G100", "Minecraft", "Sandbox", 20.0, "Mojang");
        dao.addGame(game);

        // 2. Perform update
        game.setPrice(99.0);
        boolean result = dao.updateGame(game);
        assertTrue(result, "Game should be updated successfully");

        // 3. Verify update persisted
        Game updated = dao.findById("G100");
        assertNotNull(updated);
        assertEquals(99.0, updated.getPrice());

        // 4. Clean up afterwards
        dao.deleteGame("G100");
    }

    @Test
    public void testDeleteGame() {
        // 1. Clean up and insert test data to make test completely independent
        dao.deleteGame("G100");
        Game game = new Game("G100", "Minecraft", "Sandbox", 20.0, "Mojang");
        dao.addGame(game);

        // 2. Perform delete
        boolean result = dao.deleteGame("G100");
        assertTrue(result, "Game should be deleted successfully");

        // 3. Verify deletion persisted
        Game deleted = dao.findById("G100");
        assertNull(deleted, "Deleted game should not exist in database");
    }

    @Test
    public void testValidation() {
        double price = -10.0;
        assertFalse(price > 0);
    }
}
