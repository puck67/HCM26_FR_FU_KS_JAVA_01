package org.example.dao;

import org.example.config.DatabaseConfig;
import org.example.model.Grade;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GradeDAO {

    public boolean exists(int studentId, int subjectId) {
        String sql = "SELECT COUNT(*) FROM grades WHERE student_id = ? AND subject_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            pstmt.setInt(2, subjectId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking grade existence: " + e.getMessage());
        }
        return false;
    }

    public boolean saveOrUpdate(int studentId, int subjectId, double score) {
        if (exists(studentId, subjectId)) {
            // Update
            String sql = "UPDATE grades SET score = ? WHERE student_id = ? AND subject_id = ?";
            try (Connection conn = DatabaseConfig.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setDouble(1, score);
                pstmt.setInt(2, studentId);
                pstmt.setInt(3, subjectId);

                return pstmt.executeUpdate() > 0;
            } catch (SQLException e) {
                System. err.println("Error updating grade: " + e.getMessage());
            }
        } else {
            // Insert
            String sql = "INSERT INTO grades (student_id, subject_id, score) VALUES (?, ?, ?)";
            try (Connection conn = DatabaseConfig.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, studentId);
                pstmt.setInt(2, subjectId);
                pstmt.setDouble(3, score);

                return pstmt.executeUpdate() > 0;
            } catch (SQLException e) {
                System.err.println("Error inserting grade: " + e.getMessage());
            }
        }
        return false;
    }

    public boolean delete(int studentId, int subjectId) {
        String sql = "DELETE FROM grades WHERE student_id = ? AND subject_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            pstmt.setInt(2, subjectId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting grade: " + e.getMessage());
        }
        return false;
    }

    public List<Grade> findAllWithDetails() {
        List<Grade> list = new ArrayList<>();
        String sql = "SELECT g.id, g.student_id, g.subject_id, g.score, " +
                "s.student_code, s.name AS student_name, " +
                "sub.subject_code, sub.name AS subject_name " +
                "FROM grades g " +
                "JOIN students s ON g.student_id = s.id " +
                "JOIN subjects sub ON g.subject_id = sub.id " +
                "ORDER BY s.student_code ASC, sub.subject_code ASC";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapRowToGrade(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listing grades: " + e.getMessage());
        }
        return list;
    }

    public List<Grade> findByStudentId(int studentId) {
        List<Grade> list = new ArrayList<>();
        String sql = "SELECT g.id, g.student_id, g.subject_id, g.score, " +
                "s.student_code, s.name AS student_name, " +
                "sub.subject_code, sub.name AS subject_name " +
                "FROM grades g " +
                "JOIN students s ON g.student_id = s.id " +
                "JOIN subjects sub ON g.subject_id = sub.id " +
                "WHERE g.student_id = ? " +
                "ORDER BY sub.subject_code ASC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToGrade(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding grades for student: " + e.getMessage());
        }
        return list;
    }

    public List<Grade> findBySubjectId(int subjectId) {
        List<Grade> list = new ArrayList<>();
        String sql = "SELECT g.id, g.student_id, g.subject_id, g.score, " +
                "s.student_code, s.name AS student_name, " +
                "sub.subject_code, sub.name AS subject_name " +
                "FROM grades g " +
                "JOIN students s ON g.student_id = s.id " +
                "JOIN subjects sub ON g.subject_id = sub.id " +
                "WHERE g.subject_id = ? " +
                "ORDER BY s.student_code ASC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, subjectId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToGrade(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding grades for subject: " + e.getMessage());
        }
        return list;
    }

    private Grade mapRowToGrade(ResultSet rs) throws SQLException {
        Grade grade = new Grade(
                rs.getInt("id"),
                rs.getInt("student_id"),
                rs.getInt("subject_id"),
                rs.getDouble("score")
        );
        grade.setStudentCode(rs.getString("student_code"));
        grade.setStudentName(rs.getString("student_name"));
        grade.setSubjectCode(rs.getString("subject_code"));
        grade.setSubjectName(rs.getString("subject_name"));
        return grade;
    }
}
