package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBContext {
    private static final String URL = "jdbc:h2:./appointment_db;AUTO_SERVER=TRUE";
    private static final String USER = "sa";
    private static final String PASS = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static void initializeDatabase() {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS Users (" +
                "id VARCHAR(50) PRIMARY KEY, " +
                "name VARCHAR(100) NOT NULL" +
                ");";

        String createAppointmentsTable = "CREATE TABLE IF NOT EXISTS Appointments (" +
                "id VARCHAR(50) PRIMARY KEY, " +
                "person1 VARCHAR(50), " +
                "person2 VARCHAR(50), " +
                "startTime VARCHAR(50), " +
                "endTime VARCHAR(50), " +
                "place VARCHAR(100), " +
                "reason VARCHAR(255), " +
                "FOREIGN KEY (person1) REFERENCES Users(id), " +
                "FOREIGN KEY (person2) REFERENCES Users(id)" +
                ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createUsersTable);
            stmt.execute(createAppointmentsTable);
            System.out.println("Database initialized successfully.");
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }
}
