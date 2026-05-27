package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBConnection {

    // H2 connection URL with AUTO_SERVER=true to allow multiple processes/tools
    // (like VS Code Test Runner, the main App, and H2 Console) to connect simultaneously!
    private static final String URL = "jdbc:h2:./game_management;AUTO_SERVER=true;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";
    private static boolean isInitialized = false;

    public static Connection getConnection() {
        try {
            Class.forName("org.h2.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            if (!isInitialized) {
                initializeDatabase(conn);
                isInitialized = true;
            }
            return conn;
        } catch (Exception e) {
            System.err.println("Failed to connect to H2 database: " + e.getMessage());
            return null;
        }
    }

    private static void initializeDatabase(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            // 1. Create table 'games' if it does not exist
            stmt.execute("CREATE TABLE IF NOT EXISTS games (" +
                    "id VARCHAR(10) PRIMARY KEY, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "genre VARCHAR(100), " +
                    "price DOUBLE, " +
                    "developer VARCHAR(100)" +
                    ")");

            // 2. Drop old aliases to prevent duplicates
            stmt.execute("DROP ALIAS IF EXISTS insert_game");
            stmt.execute("DROP ALIAS IF EXISTS get_all_games");
            stmt.execute("DROP ALIAS IF EXISTS find_game_by_id");
            stmt.execute("DROP ALIAS IF EXISTS update_game");
            stmt.execute("DROP ALIAS IF EXISTS delete_game");

            // 3. Create H2 Java aliases for Stored Procedures (allowing JDBC CallableStatement to run unchanged!)
            
            // Procedure: insert_game
            stmt.execute("CREATE ALIAS insert_game AS $$\n" +
                    "void insertGame(java.sql.Connection conn, String id, String name, String genre, double price, String developer) throws java.sql.SQLException {\n" +
                    "    java.sql.PreparedStatement stmt = conn.prepareStatement(\"INSERT INTO games(id, name, genre, price, developer) VALUES(?, ?, ?, ?, ?)\");\n" +
                    "    stmt.setString(1, id);\n" +
                    "    stmt.setString(2, name);\n" +
                    "    stmt.setString(3, genre);\n" +
                    "    stmt.setDouble(4, price);\n" +
                    "    stmt.setString(5, developer);\n" +
                    "    stmt.executeUpdate();\n" +
                    "}\n" +
                    "$$;");

            // Procedure: get_all_games
            stmt.execute("CREATE ALIAS get_all_games AS $$\n" +
                    "java.sql.ResultSet getAllGames(java.sql.Connection conn) throws java.sql.SQLException {\n" +
                    "    return conn.createStatement().executeQuery(\"SELECT * FROM games\");\n" +
                    "}\n" +
                    "$$;");

            // Procedure: find_game_by_id
            stmt.execute("CREATE ALIAS find_game_by_id AS $$\n" +
                    "java.sql.ResultSet findGameById(java.sql.Connection conn, String id) throws java.sql.SQLException {\n" +
                    "    java.sql.PreparedStatement stmt = conn.prepareStatement(\"SELECT * FROM games WHERE id = ?\");\n" +
                    "    stmt.setString(1, id);\n" +
                    "    return stmt.executeQuery();\n" +
                    "}\n" +
                    "$$;");

            // Procedure: update_game
            stmt.execute("CREATE ALIAS update_game AS $$\n" +
                    "void updateGame(java.sql.Connection conn, String id, String name, String genre, double price, String developer) throws java.sql.SQLException {\n" +
                    "    java.sql.PreparedStatement stmt = conn.prepareStatement(\"UPDATE games SET name = ?, genre = ?, price = ?, developer = ? WHERE id = ?\");\n" +
                    "    stmt.setString(1, name);\n" +
                    "    stmt.setString(2, genre);\n" +
                    "    stmt.setDouble(3, price);\n" +
                    "    stmt.setString(4, developer);\n" +
                    "    stmt.setString(5, id);\n" +
                    "    stmt.executeUpdate();\n" +
                    "}\n" +
                    "$$;");

            // Procedure: delete_game
            stmt.execute("CREATE ALIAS delete_game AS $$\n" +
                    "void deleteGame(java.sql.Connection conn, String id) throws java.sql.SQLException {\n" +
                    "    java.sql.PreparedStatement stmt = conn.prepareStatement(\"DELETE FROM games WHERE id = ?\");\n" +
                    "    stmt.setString(1, id);\n" +
                    "    stmt.executeUpdate();\n" +
                    "}\n" +
                    "$$;");

            System.out.println(">>> [H2 Database] Successfully initialized schema and Stored Procedure aliases!");
        } catch (Exception e) {
            System.err.println(">>> [H2 Database] Error initializing schema: " + e.getMessage());
        }
    }
}
