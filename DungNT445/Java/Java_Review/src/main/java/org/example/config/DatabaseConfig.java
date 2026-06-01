package org.example.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class DatabaseConfig {
    // File-based H2 database path: ./data/student_db
    // AUTO_SERVER=TRUE allows multiple processes to connect to it simultaneously (useful for debugging)
    private static final String DB_URL = "jdbc:h2:./data/student_db;AUTO_SERVER=TRUE;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    static {
        try {
            // Load H2 driver explicitly
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("H2 JDBC Driver not found. Please ensure H2 is in dependencies.");
            e.printStackTrace();
        }
    }

    /**
     * Get a connection to the database
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    /**
     * Create tables and insert sample seed data if empty
     */
    public static void initializeDatabase() {
        String createStudentsTable = "CREATE TABLE IF NOT EXISTS students (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "student_code VARCHAR(50) NOT NULL UNIQUE, " +
                "name VARCHAR(100) NOT NULL, " +
                "email VARCHAR(100) NOT NULL UNIQUE, " +
                "phone VARCHAR(20) NOT NULL" +
                ");";

        String createSubjectsTable = "CREATE TABLE IF NOT EXISTS subjects (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "subject_code VARCHAR(50) NOT NULL UNIQUE, " +
                "name VARCHAR(100) NOT NULL, " +
                "credits INT NOT NULL" +
                ");";

        String createGradesTable = "CREATE TABLE IF NOT EXISTS grades (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "student_id INT NOT NULL, " +
                "subject_id INT NOT NULL, " +
                "score DOUBLE NOT NULL, " +
                "FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE, " +
                "FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE, " +
                "CONSTRAINT unique_student_subject UNIQUE (student_id, subject_id)" +
                ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Create schema
            stmt.execute(createStudentsTable);
            stmt.execute(createSubjectsTable);
            stmt.execute(createGradesTable);

            // Register stored procedure aliases
            stmt.execute("CREATE ALIAS IF NOT EXISTS add_student FOR \"org.example.config.StudentProcedures.addStudent\"");
            stmt.execute("CREATE ALIAS IF NOT EXISTS update_student FOR \"org.example.config.StudentProcedures.updateStudent\"");
            stmt.execute("CREATE ALIAS IF NOT EXISTS delete_student FOR \"org.example.config.StudentProcedures.deleteStudent\"");



//            stmt.execute("CREATE ALIAS IF NOT EXISTS add_grade FOR \"org.example.config.GradeProcedures.addGrade\"");
//            stmt.execute("CREATE ALIAS IF NOT EXISTS update_grade FOR \"org.example.config.GradeProcedures.updateGrade\"");
//            stmt.execute("CREATE ALIAS IF NOT EXISTS delete_grade FOR \"org.example.config.GradeProcedures.deleteGrade\"");


//            stmt.execute("CREATE ALIAS IF NOT EXISTS add_student FOR \"org.example.config.DatabaseProcedures.addStudent\"");
//            stmt.execute("CREATE ALIAS IF NOT EXISTS update_student FOR \"org.example.config.DatabaseProcedures.updateStudent\"");
//            stmt.execute("CREATE ALIAS IF NOT EXISTS delete_student FOR \"org.example.config.DatabaseProcedures.deleteStudent\"");



            // Seed data if tables are empty
            seedInitialData(conn);

            System.out.println("Database successfully initialized.");

        } catch (SQLException e) {
            System.err.println("Error initializing database schema:");
            e.printStackTrace();
        }
    }

    private static void seedInitialData(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM students")) {
            if (rs.next() && rs.getInt(1) == 0) {
                System.out.println("Seeding sample students...");
                stmt.execute("INSERT INTO students (student_code, name, email, phone) VALUES " +
                        "('SV001', 'Nguyen Van A', 'a.nguyen@gmail.com', '0912345678')," +
                        "('SV002', 'Tran Thi B', 'b.tran@gmail.com', '0987654321')," +
                        "('SV003', 'Le Hoang C', 'c.le@gmail.com', '0901234567');");
            }
        }

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM subjects")) {
            if (rs.next() && rs.getInt(1) == 0) {
                System.out.println("Seeding sample subjects...");
                stmt.execute("INSERT INTO subjects (subject_code, name, credits) VALUES " +
                        "('MH01', 'Lap trinh Java', 3)," +
                        "('MH02', 'Co so du lieu', 3)," +
                        "('MH03', 'Cau truc du lieu', 4);");
            }
        }

        // Seed Grades if empty
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM grades")) {
            if (rs.next() && rs.getInt(1) == 0) {
                System.out.println("Seeding sample grades...");

                stmt.execute("INSERT INTO grades (student_id, subject_id, score) VALUES " +
                        "(1, 1, 8.5)," +
                        "(2, 1, 9.0)," +
                        "(2, 2, 7.5);");
            }
        }
    }
}
