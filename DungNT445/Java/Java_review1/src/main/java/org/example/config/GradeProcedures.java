package org.example.config;

import java.sql.*;

public class GradeProcedures {
    public static int addGrades(Connection conn, String student_id, String subject_id, double score) throws SQLException {
        String sql = "INSERT INTO grades (student_id, subject_id, score) VALUES (?,?,?) ";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, student_id);
            pstmt.setString(2, subject_id);
            pstmt.setDouble(3, score);
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

    public static void updateGrades(Connection conn, String student_id, String subject_id, double score) throws SQLException {
        String sql = "UPDATE grades SET student_id = ?, subject_id = ?, score = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, student_id);
            pstmt.setString(2, subject_id);
            pstmt.setDouble(3, score);
            pstmt.executeUpdate();
        }
    }

    public static void deleteGrades(Connection conn, int id) throws SQLException {
        String sql = "DELETE FROM grades WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}
