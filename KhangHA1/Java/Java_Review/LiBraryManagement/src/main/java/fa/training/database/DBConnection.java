package fa.training.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {

    static {
        try {
            Class.forName(PropertyManager.getDriver());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("JDBC driver not found: " + PropertyManager.getDriver(), e);
        }
    }

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                PropertyManager.getUrl(),
                PropertyManager.getUsername(),
                PropertyManager.getPassword()
        );
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
