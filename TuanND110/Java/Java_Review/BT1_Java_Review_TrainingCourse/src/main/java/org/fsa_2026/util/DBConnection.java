package org.fsa_2026.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {
    private static final String DEFAULT_URL = "jdbc:postgresql://localhost:5432/trainingcourse";
    private static final String DEFAULT_USER = "postgres";
    private static final String DEFAULT_PASSWORD = "postgres";

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        String url = getEnvOrDefault("DB_URL", DEFAULT_URL);
        String user = getEnvOrDefault("DB_USER", DEFAULT_USER);
        String password = getEnvOrDefault("DB_PASSWORD", DEFAULT_PASSWORD);
        return DriverManager.getConnection(url, user, password);
    }

    private static String getEnvOrDefault(String key, String fallback) {
        String value = System.getenv(key);
        return value == null || value.trim().isEmpty() ? fallback : value.trim();
    }
}
