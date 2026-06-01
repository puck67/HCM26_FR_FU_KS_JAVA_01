package fa.training.config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class HibernateUtils {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            ensureDatabaseExists();
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed. " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static void ensureDatabaseExists() {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "tintinkute22";
        String dbName = "movietheaterdb";

        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, user, password);
                 Statement stmt = conn.createStatement()) {
                
                ResultSet rs = stmt.executeQuery("SELECT 1 FROM pg_database WHERE datname = '" + dbName + "'");
                if (!rs.next()) {
                    System.out.println("Database '" + dbName + "' không tồn tại. Đang tự động khởi tạo...");
                    stmt.executeUpdate("CREATE DATABASE " + dbName);
                    System.out.println("Tạo database '" + dbName + "' thành công!");
                } else {
                    System.out.println("Database '" + dbName + "' đã tồn tại.");
                }
            }
        } catch (Exception e) {
            System.err.println("Lưu ý: Không thể tự động kiểm tra/tạo database '" + dbName + "': " + e.getMessage());
            System.err.println("Vui lòng đảm bảo database '" + dbName + "' đã được tạo thủ công trong PostgreSQL.");
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}
