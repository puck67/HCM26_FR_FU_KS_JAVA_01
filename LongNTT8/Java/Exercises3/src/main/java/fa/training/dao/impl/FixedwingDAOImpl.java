package fa.training.dao.impl;

import fa.training.dao.FixedwingDAO;
import fa.training.database.DBUtil;
import fa.training.model.Fixedwing;
import fa.training.model.PlaneType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FixedwingDAOImpl implements FixedwingDAO {

    @Override
    public void create(Fixedwing fw) {
        String sql = "INSERT INTO Fixedwing (id, model, cruiseSpeed, emptyWeight, maxTakeoffWeight, planeType, minNeededRunwaySize) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setFixedwingParams(pstmt, fw);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Fixedwing fw) {
        String sql = "UPDATE Fixedwing SET model = ?, cruiseSpeed = ?, emptyWeight = ?, "
                + "maxTakeoffWeight = ?, planeType = ?, minNeededRunwaySize = ? "
                + "WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, fw.getModel());
            pstmt.setDouble(2, fw.getCruiseSpeed());
            pstmt.setDouble(3, fw.getEmptyWeight());
            pstmt.setDouble(4, fw.getMaxTakeoffWeight());
            pstmt.setString(5, fw.getPlaneType().name());
            pstmt.setDouble(6, fw.getMinNeededRunwaySize());
            pstmt.setString(7, fw.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM Fixedwing WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Fixedwing getFixedwingById(String id) {
        String sql = "SELECT * FROM Fixedwing WHERE id = ?";

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
    public List<Fixedwing> getAllFixedwings() {
        String sql = "SELECT * FROM Fixedwing";
        List<Fixedwing> list = new ArrayList<>();

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
    public void createFixedwingSP(Fixedwing fw) {
        String sql = "{call usp_CreateFixedwing(?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conn = DBUtil.getConnection();
                CallableStatement cstmt = conn.prepareCall(sql)) {

            setFixedwingCallParams(cstmt, fw);
            cstmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateFixedwingSP(Fixedwing fw) {
        String sql = "{call usp_UpdateFixedwing(?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conn = DBUtil.getConnection();
                CallableStatement cstmt = conn.prepareCall(sql)) {

            setFixedwingCallParams(cstmt, fw);
            cstmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteFixedwingSP(String id) {
        String sql = "{call usp_DeleteFixedwing(?)}";

        try (Connection conn = DBUtil.getConnection();
                CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setString(1, id);
            cstmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Map một dòng ResultSet thành đối tượng Fixedwing
    private Fixedwing mapRow(ResultSet rs) throws SQLException {
        return new Fixedwing(
                rs.getString("id"),
                rs.getString("model"),
                rs.getDouble("cruiseSpeed"),
                rs.getDouble("emptyWeight"),
                rs.getDouble("maxTakeoffWeight"),
                PlaneType.valueOf(rs.getString("planeType")),
                rs.getDouble("minNeededRunwaySize"));
    }

    // Set tham số cho INSERT statement (7 params)
    private void setFixedwingParams(PreparedStatement pstmt, Fixedwing fw) throws SQLException {
        pstmt.setString(1, fw.getId());
        pstmt.setString(2, fw.getModel());
        pstmt.setDouble(3, fw.getCruiseSpeed());
        pstmt.setDouble(4, fw.getEmptyWeight());
        pstmt.setDouble(5, fw.getMaxTakeoffWeight());
        pstmt.setString(6, fw.getPlaneType().name());
        pstmt.setDouble(7, fw.getMinNeededRunwaySize());
    }

    // Set tham số cho Stored Procedure call (7 params)
    private void setFixedwingCallParams(CallableStatement cstmt, Fixedwing fw) throws SQLException {
        cstmt.setString(1, fw.getId());
        cstmt.setString(2, fw.getModel());
        cstmt.setDouble(3, fw.getCruiseSpeed());
        cstmt.setDouble(4, fw.getEmptyWeight());
        cstmt.setDouble(5, fw.getMaxTakeoffWeight());
        cstmt.setString(6, fw.getPlaneType().name());
        cstmt.setDouble(7, fw.getMinNeededRunwaySize());
    }
}
