package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName(Constants.DB_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return java.sql.DriverManager.getConnection(Constants.DB_URL, Constants.DB_USER, Constants.DB_PASSWORD);
    }
}