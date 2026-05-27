package dao;

import database.DBConnection;
import model.Game;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GameDAO {

    // Mock database for offline testing when H2/MySQL server is not running
    private static final List<Game> mockDatabase = new ArrayList<>();
    private static boolean isOfflineWarningPrinted = false;

    static {
        // Pre-populate mock database with some sample data for easy offline testing
        mockDatabase.add(new Game("G001", "Minecraft", "Sandbox", 20.0, "Mojang"));
        mockDatabase.add(new Game("G002", "FIFA 24", "Sports", 60.0, "EA Sports"));
    }

    private void checkConnectionAndNotify(Connection conn) {
        if (conn == null && !isOfflineWarningPrinted) {
            System.out.println("\n>>> [WARNING] Cannot connect to Database!");
            System.out.println(">>> Activating: IN-MEMORY DEMO MODE (Offline Simulation) so you can test all features.\n");
            isOfflineWarningPrinted = true;
        }
    }

    public boolean addGame(Game game) {
        Connection conn = DBConnection.getConnection();
        checkConnectionAndNotify(conn);

        if (conn == null) {
            // Offline simulation logic
            if (findById(game.getId()) != null) {
                return false; // Duplicate key
            }
            mockDatabase.add(game);
            return true;
        }

        try (
                Connection c = conn;
                CallableStatement stmt = c.prepareCall("{call insert_game(?,?,?,?,?)}")
        ) {
            stmt.setString(1, game.getId());
            stmt.setString(2, game.getName());
            stmt.setString(3, game.getGenre());
            stmt.setDouble(4, game.getPrice());
            stmt.setString(5, game.getDeveloper());
            stmt.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Game> getAllGames() {
        Connection conn = DBConnection.getConnection();
        checkConnectionAndNotify(conn);

        if (conn == null) {
            // Offline simulation logic
            return new ArrayList<>(mockDatabase);
        }

        List<Game> games = new ArrayList<>();
        try (
                Connection c = conn;
                CallableStatement stmt = c.prepareCall("{call get_all_games()}");
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                Game game = new Game(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("genre"),
                        rs.getDouble("price"),
                        rs.getString("developer")
                );
                games.add(game);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return games;
    }

    public Game findById(String id) {
        Connection conn = DBConnection.getConnection();
        checkConnectionAndNotify(conn);

        if (conn == null) {
            // Offline simulation logic
            return mockDatabase.stream()
                    .filter(g -> g.getId().equalsIgnoreCase(id))
                    .findFirst()
                    .orElse(null);
        }

        try (
                Connection c = conn;
                CallableStatement stmt = c.prepareCall("{call find_game_by_id(?)}")
        ) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Game(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("genre"),
                        rs.getDouble("price"),
                        rs.getString("developer")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateGame(Game game) {
        Connection conn = DBConnection.getConnection();
        checkConnectionAndNotify(conn);

        if (conn == null) {
            // Offline simulation logic
            Game existing = findById(game.getId());
            if (existing != null) {
                existing.setName(game.getName());
                existing.setGenre(game.getGenre());
                existing.setPrice(game.getPrice());
                existing.setDeveloper(game.getDeveloper());
                return true;
            }
            return false;
        }

        try (
                Connection c = conn;
                CallableStatement stmt = c.prepareCall("{call update_game(?,?,?,?,?)}")
        ) {
            stmt.setString(1, game.getId());
            stmt.setString(2, game.getName());
            stmt.setString(3, game.getGenre());
            stmt.setDouble(4, game.getPrice());
            stmt.setString(5, game.getDeveloper());
            stmt.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteGame(String id) {
        Connection conn = DBConnection.getConnection();
        checkConnectionAndNotify(conn);

        if (conn == null) {
            // Offline simulation logic
            return mockDatabase.removeIf(g -> g.getId().equalsIgnoreCase(id));
        }

        try (
                Connection c = conn;
                CallableStatement stmt = c.prepareCall("{call delete_game(?)}")
        ) {
            stmt.setString(1, id);
            stmt.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
