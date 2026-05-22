package org.example.config;

import java.sql.*;

public class StudentProcedures {
    public static int addStudent(Connection conn, String code, String name, String email, String phone) throws SQLException {
        String sql = "INSERT INTO students (student_code, name, email, phone) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, code);
            pstmt.setString(2, name);
            pstmt.setString(3, email);
            pstmt.setString(4, phone);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        }
        return -1;
    }


    public static void updateStudent(Connection conn, int id, String code, String name, String email, String phone) throws SQLException {
        String sql = "UPDATE students SET student_code = ?, name = ?, email = ?, phone = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, code);
            pstmt.setString(2, name);
            pstmt.setString(3, email);
            pstmt.setString(4, phone);
            pstmt.setInt(5, id);
            pstmt.executeUpdate();
        }
    }


    public static void deleteStudent(Connection conn, int id) throws SQLException {
        String sql = "DELETE FROM students WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}
