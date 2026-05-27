package fa.training;

import fa.training.database.DBUtil;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;

public class InitDB {
    public static void main(String[] args) {
        try {
            String sql = new String(Files.readAllBytes(Paths.get("f:/Exercises3/db_setup.sql", new String[0])));
            try (Connection conn = DBUtil.getConnection();
                 Statement stmt = conn.createStatement();){
                stmt.execute(sql);
                System.out.println("Database tables created successfully.");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

