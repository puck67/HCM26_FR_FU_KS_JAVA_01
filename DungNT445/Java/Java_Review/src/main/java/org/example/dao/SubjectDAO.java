package org.example.dao;

import org.example.config.DatabaseConfig;
import org.example.model.Subject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectDAO {

    public boolean insert(Subject subject) {
        String sql = "INSERT INTO subjects (subject_code, name, credits) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, subject.getSubjectCode());
            pstmt.setString(2, subject.getName());
            pstmt.setInt(3, subject.getCredits());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        subject.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error inserting subject: " + e.getMessage());
        }
        return false;
    }

    public boolean update(Subject subject) {
        String sql = "UPDATE subjects SET subject_code = ?, name = ?, credits = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, subject.getSubjectCode());
            pstmt.setString(2, subject.getName());
            pstmt.setInt(3, subject.getCredits());
            pstmt.setInt(4, subject.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating subject: " + e.getMessage());
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM subjects WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting subject: " + e.getMessage());
        }
        return false;
    }

    public Subject findById(int id) {
        String sql = "SELECT * FROM subjects WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToSubject(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding subject by ID: " + e.getMessage());
        }
        return null;
    }

    public Subject findByCode(String subjectCode) {
        String sql = "SELECT * FROM subjects WHERE subject_code = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, subjectCode);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToSubject(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding subject by code: " + e.getMessage());
        }
        return null;
    }

    public List<Subject> findAll() {
        List<Subject> list = new ArrayList<>();
        String sql = "SELECT * FROM subjects ORDER BY subject_code ASC";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapRowToSubject(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listing subjects: " + e.getMessage());
        }
        return list;
    }

    private Subject mapRowToSubject(ResultSet rs) throws SQLException {
        return new Subject(
                rs.getInt("id"),
                rs.getString("subject_code"),
                rs.getString("name"),
                rs.getInt("credits")
        );
    }
}
