package com.attendance.repository;

import com.attendance.model.AttendanceRecord;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AttendanceRepositoryImpl implements AttendanceRepository {

    @Override
    public void save(AttendanceRecord record) {
        String sql = "INSERT INTO attendance_records (employee_id, employee_name, date, check_in, check_out, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, record.getEmployeeId());
            ps.setString(2, record.getEmployeeName());
            ps.setDate(3, Date.valueOf(record.getDate()));
            ps.setTime(4, record.getCheckIn() != null ? Time.valueOf(record.getCheckIn()) : null);
            ps.setTime(5, record.getCheckOut() != null ? Time.valueOf(record.getCheckOut()) : null);
            ps.setString(6, record.getStatus());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) record.setId(keys.getInt(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public AttendanceRecord findById(int id) {
        String sql = "SELECT * FROM attendance_records WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<AttendanceRecord> findAll() {
        List<AttendanceRecord> list = new ArrayList<>();
        String sql = "SELECT * FROM attendance_records ORDER BY date DESC, id DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<AttendanceRecord> findByEmployeeId(String employeeId) {
        List<AttendanceRecord> list = new ArrayList<>();
        String sql = "SELECT * FROM attendance_records WHERE employee_id = ? ORDER BY date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, employeeId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<AttendanceRecord> findByDate(LocalDate date) {
        List<AttendanceRecord> list = new ArrayList<>();
        String sql = "SELECT * FROM attendance_records WHERE date = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(date));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void update(AttendanceRecord record) {
        String sql = "UPDATE attendance_records SET check_in=?, check_out=?, status=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTime(1, record.getCheckIn() != null ? Time.valueOf(record.getCheckIn()) : null);
            ps.setTime(2, record.getCheckOut() != null ? Time.valueOf(record.getCheckOut()) : null);
            ps.setString(3, record.getStatus());
            ps.setInt(4, record.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM attendance_records WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private AttendanceRecord mapRow(ResultSet rs) throws SQLException {
        AttendanceRecord r = new AttendanceRecord();
        r.setId(rs.getInt("id"));
        r.setEmployeeId(rs.getString("employee_id"));
        r.setEmployeeName(rs.getString("employee_name"));
        r.setDate(rs.getDate("date").toLocalDate());
        Time ci = rs.getTime("check_in");
        Time co = rs.getTime("check_out");
        if (ci != null) r.setCheckIn(ci.toLocalTime());
        if (co != null) r.setCheckOut(co.toLocalTime());
        r.setStatus(rs.getString("status"));
        return r;
    }
}
