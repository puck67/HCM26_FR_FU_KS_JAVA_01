package dao;

import db.DBConnection;
import model.Teacher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherDAO {
    
    // 1. Add a teacher
    public boolean add(Teacher teacher) {
        String sql = "{call insert_teacher(?, ?, ?, ?, ?)}";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, teacher.getId());
            cs.setString(2, teacher.getName());
            cs.setString(3, teacher.getEmail());
            cs.setString(4, teacher.getPhone());
            cs.setDouble(5, teacher.getSalary());

            int rows = cs.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("[DAO] add() failed: " + e.getMessage());
            return false;
        }
    }
    
    // 2. Get all teachers
    public List<Teacher> getAll() {
        List<Teacher> list = new ArrayList<>();
        String sql = "{call get_all_teachers()}";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (SQLException e) {
            System.err.println("[DAO] getAll() failed: " + e.getMessage());
        }
        return list;
    }
    
    // 3. Update a teacher
    public boolean update(Teacher teacher) {
        String sql = "{call update_teacher(?, ?, ?, ?, ?)}";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, teacher.getId());
            cs.setString(2, teacher.getName());
            cs.setString(3, teacher.getEmail());
            cs.setString(4, teacher.getPhone());
            cs.setDouble(5, teacher.getSalary());

            int rows = cs.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("[DAO] update() failed: " + e.getMessage());
            return false;
        }
    }


    // 4. Delete a teacher
    public boolean delete(String id) {
        String sql = "{call delete_teacher(?)}";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, id);
            int rows = cs.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("[DAO] delete() failed: " + e.getMessage());
            return false;
        }
    }


    // 5. Find a teacher by ID
    public Teacher findById(String id) {
        String sql = "{call find_teacher_by_id(?)}";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, id);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("[DAO] findById() failed: " + e.getMessage());
        }
        return null;
    }

    // 6. Find a teacher by Email
    public Teacher findByEmail(String email) {
        String sql = "{call find_teacher_by_email(?)}";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, email);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[DAO] findByEmail() failed: " + e.getMessage());
        }
        return null;
    }

    // 7. Find a teacher by Phone
    public Teacher findByPhone(String phone) {
        String sql = "{call find_teacher_by_phone(?)}";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, phone);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[DAO] findByPhone() failed: " + e.getMessage());
        }
        return null;
    }

    private Teacher mapRow(ResultSet rs) throws SQLException {
        return new Teacher(
            rs.getString("id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("phone"),
            rs.getDouble("salary")
        );
    }
}
