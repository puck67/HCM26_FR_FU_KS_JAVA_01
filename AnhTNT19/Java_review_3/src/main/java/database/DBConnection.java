package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static String url =
        "jdbc:h2:./data/quiz_db;INIT=RUNSCRIPT FROM 'classpath:schema.sql'";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, USER, PASSWORD);
    }

    public static void setUrl(String newUrl) {
        url = newUrl;
    }
}
