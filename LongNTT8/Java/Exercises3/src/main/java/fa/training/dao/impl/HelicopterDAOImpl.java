package fa.training.dao.impl;

import fa.training.dao.HelicopterDAO;
import fa.training.database.DBUtil;
import fa.training.model.Helicopter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HelicopterDAOImpl implements HelicopterDAO {

    @Override
    public void create(Helicopter h) {
        String sql = "INSERT INTO Helicopter (id, model, cruiseSpeed, emptyWeight, maxTakeoffWeight, range) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setHelicopterParams(pstmt, h);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Helicopter h) {
        String sql = "UPDATE Helicopter SET model = ?, cruiseSpeed = ?, emptyWeight = ?, "
                + "maxTakeoffWeight = ?, range = ? "
                + "WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, h.getModel());
            pstmt.setDouble(2, h.getCruiseSpeed());
            pstmt.setDouble(3, h.getEmptyWeight());
            pstmt.setDouble(4, h.getMaxTakeoffWeight());
            pstmt.setDouble(5, h.getRange());
            pstmt.setString(6, h.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM Helicopter WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Helicopter getHelicopterById(String id) {
        String sql = "SELECT * FROM Helicopter WHERE id = ?";

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
    public List<Helicopter> getAllHelicopters() {
        String sql = "SELECT * FROM Helicopter";
        List<Helicopter> list = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void createHelicopterSP(Helicopter h) {
        String sql = "{call usp_CreateHelicopter(?, ?, ?, ?, ?, ?)}";

        try (Connection conn = DBUtil.getConnection();
                CallableStatement cstmt = conn.prepareCall(sql)) {

            setHelicopterCallParams(cstmt, h);
            cstmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateHelicopterSP(Helicopter h) {
        String sql = "{call usp_UpdateHelicopter(?, ?, ?, ?, ?, ?)}";

        try (Connection conn = DBUtil.getConnection();
                CallableStatement cstmt = conn.prepareCall(sql)) {

            setHelicopterCallParams(cstmt, h);
            cstmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteHelicopterSP(String id) {
        String sql = "{call usp_DeleteHelicopter(?)}";

        try (Connection conn = DBUtil.getConnection();
                CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setString(1, id);
            cstmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Map một dòng ResultSet thành đối tượng Helicopter
    private Helicopter mapRow(ResultSet rs) throws SQLException {
        return new Helicopter(
                rs.getString("id"),
                rs.getString("model"),
                rs.getDouble("cruiseSpeed"),
                rs.getDouble("emptyWeight"),
                rs.getDouble("maxTakeoffWeight"),
                rs.getDouble("range"));
    }

    // Set tham số cho INSERT statement (6 params)
    private void setHelicopterParams(PreparedStatement pstmt, Helicopter h) throws SQLException {
        pstmt.setString(1, h.getId());
        pstmt.setString(2, h.getModel());
        pstmt.setDouble(3, h.getCruiseSpeed());
        pstmt.setDouble(4, h.getEmptyWeight());
        pstmt.setDouble(5, h.getMaxTakeoffWeight());
        pstmt.setDouble(6, h.getRange());
    }

    // Set tham số cho Stored Procedure call (6 params)
    private void setHelicopterCallParams(CallableStatement cstmt, Helicopter h) throws SQLException {
        cstmt.setString(1, h.getId());
        cstmt.setString(2, h.getModel());
        cstmt.setDouble(3, h.getCruiseSpeed());
        cstmt.setDouble(4, h.getEmptyWeight());
        cstmt.setDouble(5, h.getMaxTakeoffWeight());
        cstmt.setDouble(6, h.getRange());
    }
}
