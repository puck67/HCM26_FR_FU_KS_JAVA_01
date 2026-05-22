package fa.training.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    private static final String URL = "jdbc:h2:./data/bookmanager;AUTO_SERVER=TRUE";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    static {
        try {
            // Load H2 driver
            Class.forName("org.h2.Driver");
            
            // Automatically initialize table if it does not exist
            try (Connection conn = getConnection();
                 Statement stmt = conn.createStatement()) {
                stmt.execute("CREATE TABLE IF NOT EXISTS books (" +
                        "id VARCHAR(10) PRIMARY KEY, " +
                        "title VARCHAR(100) NOT NULL, " +
                        "author VARCHAR(100) NOT NULL, " +
                        "email VARCHAR(100) NOT NULL, " +
                        "phone VARCHAR(20) NOT NULL, " +
                        "price DOUBLE NOT NULL, " +
                        "quantity INT NOT NULL, " +
                        "category VARCHAR(50) NOT NULL" +
                        ")");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("H2 JDBC Driver not found. Ensure you have the jar file in the classpath.");
        } catch (SQLException e) {
            System.err.println("[DB ERROR] Initializing database failed: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
