package org.fsa_2026.daoImpl;

import org.fsa_2026.dao.TrainnerDao;
import org.fsa_2026.enities.Trainner;
import org.fsa_2026.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TrainnerDaoImpl implements TrainnerDao {

    @Override
    public boolean insert(Trainner trainner) {
        String sql = "INSERT INTO trainers (trainer_name, phone, email, birth_date, class_name) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, trainner.getTrainnerName());
            ps.setString(2, trainner.getPhone());
            ps.setString(3, trainner.getEmail());
            setDateOrNull(ps, 4, trainner.getBirthDate());
            ps.setString(5, trainner.getClassName());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Insert trainer failed", e);
        }
    }

    @Override
    public Trainner findById(int id) {
        String sql = "SELECT id, trainer_name, phone, email, birth_date, class_name FROM trainers WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Find trainer by id failed", e);
        }
        return null;
    }

    @Override
    public Trainner findByName(String name) {
        String sql = "SELECT id, trainer_name, phone, email, birth_date, class_name FROM trainers WHERE trainer_name = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Find trainer by name failed", e);
        }
        return null;
    }

    @Override
    public List<Trainner> findAll() {
        String sql = "SELECT id, trainer_name, phone, email, birth_date, class_name FROM trainers ORDER BY id";
        List<Trainner> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Find all trainers failed", e);
        }
        return list;
    }

    @Override
    public boolean update(Trainner trainner) {
        String sql = "UPDATE trainers SET trainer_name = ?, phone = ?, email = ?, birth_date = ?, class_name = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, trainner.getTrainnerName());
            ps.setString(2, trainner.getPhone());
            ps.setString(3, trainner.getEmail());
            setDateOrNull(ps, 4, trainner.getBirthDate());
            ps.setString(5, trainner.getClassName());
            ps.setInt(6, trainner.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Update trainer failed", e);
        }
    }

    @Override
    public boolean deleteById(int id) {
        String sql = "DELETE FROM trainers WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Delete trainer failed", e);
        }
    }

    private static Trainner mapRow(ResultSet rs) throws SQLException {
        Trainner trainner = new Trainner();
        trainner.setId(rs.getInt("id"));
        trainner.setTrainnerName(rs.getString("trainer_name"));
        trainner.setPhone(rs.getString("phone"));
        trainner.setEmail(rs.getString("email"));
        trainner.setBirthDate(rs.getDate("birth_date"));
        trainner.setClassName(rs.getString("class_name"));
        return trainner;
    }

    private static void setDateOrNull(PreparedStatement ps, int index, Date value) throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.DATE);
        } else {
            ps.setDate(index, new java.sql.Date(value.getTime()));
        }
    }
}

