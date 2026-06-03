package org.example.config;

import java.sql.*;

public class SubjectProcedures {
    public static int addSubject(Connection conn, String subject_code, String name, int credits) throws SQLException {
        String sql = "INSERT INTO subjects (subject_code, name, credits) VALUES (?,?,?) ";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, subject_code);
            pstmt.setString(2, name);
            pstmt.setInt(3, credits);
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

    public static void updateSubject(Connection conn, String subject_code, String name, int credits) throws SQLException {
        String sql = "UPDATE subjects SET subject_code = ?, name = ?, credits = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, subject_code);
            pstmt.setString(2, name);
            pstmt.setInt(3, credits);
            pstmt.executeUpdate();
        }
    }

    public static void deleteSubject(Connection conn, int id) throws SQLException {
        String sql = "DELETE FROM subjects WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

}
