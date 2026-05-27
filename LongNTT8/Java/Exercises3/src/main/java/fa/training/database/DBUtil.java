package fa.training.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Utility class để lấy kết nối database
public class DBUtil {

    private static final String URL = "jdbc:postgresql://localhost:5432/CSS";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123456";

    // Trả về một kết nối mới tới PostgreSQL
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
