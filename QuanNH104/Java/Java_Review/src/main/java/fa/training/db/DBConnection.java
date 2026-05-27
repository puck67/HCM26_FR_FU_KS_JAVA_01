package fa.training.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                System.err.println("Không tìm thấy file db.properties. Sử dụng cấu hình mặc định.");
                properties.setProperty("db.url", "jdbc:postgresql://localhost:5432/warehouse_management");
                properties.setProperty("db.user", "postgres");
                properties.setProperty("db.password", "postgres");
            } else {
                properties.load(input);
            }
            // Load PostgreSQL driver class
            Class.forName("org.postgresql.Driver");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Lỗi khởi tạo cấu hình kết nối Database: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.user");
        String password = properties.getProperty("db.password");
        return DriverManager.getConnection(url, user, password);
    }
}
