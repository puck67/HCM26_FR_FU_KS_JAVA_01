package org.example.dao;

import org.example.db.DBConnection;
import org.example.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class StudentDAO {

    private final Function<ResultSet, Student> ROW_MAPPER = rs -> {
        try {
            return new Student(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getDouble("gpa")
            );
        } catch (SQLException e) {
            throw new RuntimeException("Error mapping ResultSet to Student", e);
        }
    };


    private List<Student> collectResults(ResultSet rs) throws SQLException {
        List<Student> list = new ArrayList<>();
        while (rs.next()) {
            list.add(ROW_MAPPER.apply(rs));
        }
        return list;
    }


    public boolean add(Student student) {
        String sql = "CALL insert_student(?, ?, ?, ?, ?)";
        try {
            Connection conn = DBConnection.getConnection();
            try (CallableStatement stmt = conn.prepareCall(sql)) {
                stmt.setString(1, student.getId());
                stmt.setString(2, student.getName());
                stmt.setString(3, student.getEmail());
                stmt.setString(4, student.getPhone());
                stmt.setDouble(5, student.getGpa());
                stmt.execute();
            }
            if (!conn.getAutoCommit()) {
                conn.commit();
                System.out.println("[DAO] Transaction committed (autoCommit was false).");
            }
            return true;
        } catch (SQLException e) {
            System.err.println("[DAO] Error adding student: " + e.getMessage());
            return false;
        }
    }


    public List<Student> getAll() {
        String sql = "{call get_all_students()}";
        try (CallableStatement stmt = DBConnection.getConnection().prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {
            return collectResults(rs);
        } catch (SQLException e) {
            System.err.println("[DAO] Error fetching students: " + e.getMessage());
            return new ArrayList<>();
        }
    }


    public boolean update(Student student) {
        String sql = "CALL update_student(?, ?, ?, ?, ?)";
        try {
            Connection conn = DBConnection.getConnection();
            try (CallableStatement stmt = conn.prepareCall(sql)) {
                stmt.setString(1, student.getId());
                stmt.setString(2, student.getName());
                stmt.setString(3, student.getEmail());
                stmt.setString(4, student.getPhone());
                stmt.setDouble(5, student.getGpa());
                stmt.execute();
            }
            if (!conn.getAutoCommit()) {
                conn.commit();
                System.out.println("[DAO] Transaction committed (autoCommit was false).");
            }
            return true;
        } catch (SQLException e) {
            System.err.println("[DAO] Error updating student: " + e.getMessage());
            return false;
        }
    }


    public boolean delete(String id) {
        String sql = "CALL delete_student(?)";
        try {
            Connection conn = DBConnection.getConnection();
            try (CallableStatement stmt = conn.prepareCall(sql)) {
                stmt.setString(1, id);
                stmt.execute();
            }
            if (!conn.getAutoCommit()) {
                conn.commit();
                System.out.println("[DAO] Transaction committed (autoCommit was false).");
            }
            return true;
        } catch (SQLException e) {
            System.err.println("[DAO] Error deleting student: " + e.getMessage());
            return false;
        }
    }


    public Optional<Student> findById(String id) {
        String sql = "{call find_student_by_id(?)}";
        try (CallableStatement stmt = DBConnection.getConnection().prepareCall(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(ROW_MAPPER.apply(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[DAO] Error finding student: " + e.getMessage());
        }
        return Optional.empty();
    }


    public List<Student> findByName(String name) {
        String sql = "{call find_students_by_name(?)}";
        try (CallableStatement stmt = DBConnection.getConnection().prepareCall(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                return collectResults(rs);
            }
        } catch (SQLException e) {
            System.err.println("[DAO] Error searching by name: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
