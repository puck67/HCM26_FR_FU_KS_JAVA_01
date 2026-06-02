package org.fsa_2026.daoImpl;

import org.fsa_2026.dao.LearnerDao;
import org.fsa_2026.enities.Learner;
import org.fsa_2026.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LearnerDaoImpl implements LearnerDao {

    @Override
    public boolean insert(Learner learner) {
        String sql = "INSERT INTO learners (student_name, phone, email, birth_date, class_room) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, learner.getStudentName());
            ps.setString(2, learner.getPhone());
            ps.setString(3, learner.getEmail());
            setDateOrNull(ps, 4, learner.getBirthDate());
            ps.setString(5, learner.getClassRoom());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Insert learner failed", e);
        }
    }

    @Override
    public Learner findById(int id) {
        String sql = "SELECT id, student_name, phone, email, birth_date, class_room FROM learners WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Find learner by id failed", e);
        }
        return null;
    }

    @Override
    public Learner findByName(String name) {
        String sql = "SELECT id, student_name, phone, email, birth_date, class_room FROM learners WHERE student_name = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Find learner by name failed", e);
        }
        return null;
    }

    @Override
    public List<Learner> findAll() {
        String sql = "SELECT id, student_name, phone, email, birth_date, class_room FROM learners ORDER BY id";
        List<Learner> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Find all learners failed", e);
        }
        return list;
    }

    @Override
    public boolean update(Learner learner) {
        String sql = "UPDATE learners SET student_name = ?, phone = ?, email = ?, birth_date = ?, class_room = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, learner.getStudentName());
            ps.setString(2, learner.getPhone());
            ps.setString(3, learner.getEmail());
            setDateOrNull(ps, 4, learner.getBirthDate());
            ps.setString(5, learner.getClassRoom());
            ps.setInt(6, learner.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Update learner failed", e);
        }
    }

    @Override
    public boolean deleteById(int id) {
        String sql = "DELETE FROM learners WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Delete learner failed", e);
        }
    }

    private static Learner mapRow(ResultSet rs) throws SQLException {
        Learner learner = new Learner();
        learner.setId(rs.getInt("id"));
        learner.setStudentName(rs.getString("student_name"));
        learner.setPhone(rs.getString("phone"));
        learner.setEmail(rs.getString("email"));
        learner.setBirthDate(rs.getDate("birth_date"));
        learner.setClassRoom(rs.getString("class_room"));
        return learner;
    }

    private static void setDateOrNull(PreparedStatement ps, int index, Date value) throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.DATE);
        } else {
            ps.setDate(index, new java.sql.Date(value.getTime()));
        }
    }
}

