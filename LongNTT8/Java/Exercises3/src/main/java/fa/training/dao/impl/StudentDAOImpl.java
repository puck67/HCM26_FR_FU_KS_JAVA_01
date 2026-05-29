package fa.training.dao.impl;

import fa.training.dao.StudentDAO;
import fa.training.database.DBUtil;
import fa.training.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAOImpl implements StudentDAO {

    @Override
    public void create(Student student) {
        String sql = "INSERT INTO Student (id, name, age, major, email, phone) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, student.getId());
            pstmt.setString(2, student.getName());
            pstmt.setInt(3, student.getAge());
            pstmt.setString(4, student.getMajor());
            pstmt.setString(5, student.getEmail());
            pstmt.setString(6, student.getPhone());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Student student) {
        String sql = "UPDATE Student SET name = ?, age = ?, major = ?, email = ?, phone = ? WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, student.getName());
            pstmt.setInt(2, student.getAge());
            pstmt.setString(3, student.getMajor());
            pstmt.setString(4, student.getEmail());
            pstmt.setString(5, student.getPhone());
            pstmt.setString(6, student.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM Student WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Student getStudentById(String id) {
        String sql = "SELECT * FROM Student WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Student> getAllStudents() {
        String sql = "SELECT * FROM Student";
        List<Student> students = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                students.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    @Override
    public boolean isEmailExists(String email, String excludeId) {
        String sql;
        boolean hasExclude = (excludeId != null && !excludeId.trim().isEmpty());
        
        if (hasExclude) {
            sql = "SELECT COUNT(*) FROM Student WHERE email = ? AND id <> ?";
        } else {
            sql = "SELECT COUNT(*) FROM Student WHERE email = ?";
        }

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            if (hasExclude) {
                pstmt.setString(2, excludeId);
            }

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isPhoneExists(String phone, String excludeId) {
        String sql;
        boolean hasExclude = (excludeId != null && !excludeId.trim().isEmpty());
        
        if (hasExclude) {
            sql = "SELECT COUNT(*) FROM Student WHERE phone = ? AND id <> ?";
        } else {
            sql = "SELECT COUNT(*) FROM Student WHERE phone = ?";
        }

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, phone);
            if (hasExclude) {
                pstmt.setString(2, excludeId);
            }

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Student mapRow(ResultSet rs) throws SQLException {
        return new Student(
            rs.getString("id"),
            rs.getString("name"),
            rs.getInt("age"),
            rs.getString("major"),
            rs.getString("email"),
            rs.getString("phone")
        );
    }
}
