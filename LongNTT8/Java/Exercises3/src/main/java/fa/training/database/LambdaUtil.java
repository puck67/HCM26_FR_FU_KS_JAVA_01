package fa.training.database;

import java.sql.SQLException;

public class LambdaUtil {

    public static void handleSQLException(SQLRunnable action) {
        try {
            action.run();
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    @FunctionalInterface
    public interface SQLRunnable {
        void run() throws SQLException;
    }
}
