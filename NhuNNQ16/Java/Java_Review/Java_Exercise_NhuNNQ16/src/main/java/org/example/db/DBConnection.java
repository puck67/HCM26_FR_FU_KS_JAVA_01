package org.example.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnection {


    private static final String DB_URL  =
        System.getenv().getOrDefault("DB_URL",  "jdbc:postgresql://localhost:5433/student_db");
    private static final String DB_USER =
        System.getenv().getOrDefault("DB_USER", "postgres");
    private static final String DB_PASS =
        System.getenv().getOrDefault("DB_PASS", "123456");


    private static Connection instance;

    private DBConnection() {}

    public static Connection getConnection() throws SQLException {
        if (instance == null || instance.isClosed()) {
            instance = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            System.out.println("[DB] Connected to PostgreSQL successfully.");
        }
        return instance;
    }


    public static void closeConnection() {
        if (instance != null) {
            try {
                if (!instance.isClosed()) {
                    instance.close();
                    System.out.println("[DB] Connection closed.");
                }
            } catch (SQLException e) {
                System.err.println("[DB] Error closing connection: " + e.getMessage());
            }
        }
    }
}
