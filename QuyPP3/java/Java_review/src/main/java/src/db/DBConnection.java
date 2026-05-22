package src.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    private static final String URL = "jdbc:h2:./data/productdb;DB_CLOSE_DELAY=-1;AUTO_SERVER=TRUE";
    private static final String USER = "sa";
    private static final String PASSWORD = "";
    
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("H2 JDBC Driver not found. Please ensure h2 is in your classpath.");
            e.printStackTrace();
            throw new SQLException("H2 JDBC Driver not found", e);
        }
        
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            initDatabase();
        }
        return connection;
    }

    private static void initDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS Product (" +
                     "id VARCHAR(10) PRIMARY KEY, " +
                     "name VARCHAR(100) NOT NULL, " +
                     "price DOUBLE NOT NULL, " +
                     "quantity INT NOT NULL, " +
                     "category VARCHAR(50) NOT NULL)";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}
