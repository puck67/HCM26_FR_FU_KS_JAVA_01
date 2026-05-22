package org.example.dao;

import org.example.config.DatabaseConfig;
import org.example.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    public boolean insert(Student student) {
        String sql = "{? = call add_student(?, ?, ?, ?)}";
        try (Connection conn = DatabaseConfig.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.registerOutParameter(1, Types.INTEGER);
            cstmt.setString(2, student.getStudentCode());
            cstmt.setString(3, student.getName());
            cstmt.setString(4, student.getEmail());
            cstmt.setString(5, student.getPhone());

            cstmt.execute();
            int generatedId = cstmt.getInt(1);
            if (generatedId > 0) {
                student.setId(generatedId);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error inserting student via procedure: " + e.getMessage());
        }
        return false;
    }

    public boolean update(Student student) {
        String sql = "{call update_student(?, ?, ?, ?, ?)}";
        try (Connection conn = DatabaseConfig.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setInt(1, student.getId());
            cstmt.setString(2, student.getStudentCode());
            cstmt.setString(3, student.getName());
            cstmt.setString(4, student.getEmail());
            cstmt.setString(5, student.getPhone());

            cstmt.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error updating student via procedure: " + e.getMessage());
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "{call delete_student(?)}";
        try (Connection conn = DatabaseConfig.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setInt(1, id);
            cstmt.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error deleting student via procedure: " + e.getMessage());
        }
        return false;
    }

    public Student findById(int id) {
        String sql = "SELECT * FROM students WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToStudent(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding student by ID: " + e.getMessage());
        }
        return null;
    }

    public Student findByCode(String studentCode) {
        String sql = "SELECT * FROM students WHERE student_code = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentCode);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToStudent(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding student by code: " + e.getMessage());
        }
        return null;
    }

    public List<Student> findAll() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY student_code ASC";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapRowToStudent(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listing students: " + e.getMessage());
        }
        return list;
    }

    private Student mapRowToStudent(ResultSet rs) throws SQLException {
        return new Student(
                rs.getInt("id"),
                rs.getString("student_code"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("phone")
        );
    }
}
