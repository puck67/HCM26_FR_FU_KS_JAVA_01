package org.fsa_2026.daoImpl;

import org.fsa_2026.dao.CousrseDao;
import org.fsa_2026.enities.Cousrse;
import org.fsa_2026.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CousrseDaoImpl implements CousrseDao {

    @Override
    public boolean insert(Cousrse cousrse) {
        String sql = "INSERT INTO courses (course_name, description, start_time, end_time) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cousrse.getCousrseName());
            ps.setString(2, cousrse.getDescription());
            setDateOrNull(ps, 3, cousrse.getStartTime());
            setDateOrNull(ps, 4, cousrse.getEndTime());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Insert course failed", e);
        }
    }

    @Override
    public Cousrse findById(int id) {
        String sql = "SELECT id, course_name, description, start_time, end_time FROM courses WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Find course by id failed", e);
        }
        return null;
    }

    @Override
    public Cousrse findByName(String name) {
        String sql = "SELECT id, course_name, description, start_time, end_time FROM courses WHERE course_name = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Find course by name failed", e);
        }
        return null;
    }

    @Override
    public List<Cousrse> findAll() {
        String sql = "SELECT id, course_name, description, start_time, end_time FROM courses ORDER BY id";
        List<Cousrse> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Find all courses failed", e);
        }
        return list;
    }

    @Override
    public boolean update(Cousrse cousrse) {
        String sql = "UPDATE courses SET course_name = ?, description = ?, start_time = ?, end_time = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cousrse.getCousrseName());
            ps.setString(2, cousrse.getDescription());
            setDateOrNull(ps, 3, cousrse.getStartTime());
            setDateOrNull(ps, 4, cousrse.getEndTime());
            ps.setInt(5, cousrse.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Update course failed", e);
        }
    }

    @Override
    public boolean deleteById(int id) {
        String sql = "DELETE FROM courses WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Delete course failed", e);
        }
    }

    private static Cousrse mapRow(ResultSet rs) throws SQLException {
        Cousrse cousrse = new Cousrse();
        cousrse.setId(rs.getInt("id"));
        cousrse.setCousrseName(rs.getString("course_name"));
        cousrse.setDescription(rs.getString("description"));
        cousrse.setStartTime(rs.getDate("start_time"));
        cousrse.setEndTime(rs.getDate("end_time"));
        return cousrse;
    }

    private static void setDateOrNull(PreparedStatement ps, int index, Date value) throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.DATE);
        } else {
            ps.setDate(index, new java.sql.Date(value.getTime()));
        }
    }
}

