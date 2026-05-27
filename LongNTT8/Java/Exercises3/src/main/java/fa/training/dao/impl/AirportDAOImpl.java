package fa.training.dao.impl;

import fa.training.dao.AirportDAO;
import fa.training.database.DBUtil;
import fa.training.model.Airport;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AirportDAOImpl implements AirportDAO {

    @Override
    public void create(Airport airport) {
        String sql = "INSERT INTO Airport (id, name, runwaySize, maxFixedWingParkingPlace, maxRotatedWingParkingPlace) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, airport.getId());
            pstmt.setString(2, airport.getName());
            pstmt.setDouble(3, airport.getRunwaySize());
            pstmt.setInt(4, airport.getMaxFixedWingParkingPlace());
            pstmt.setInt(5, airport.getMaxRotatedWingParkingPlace());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Airport airport) {
        String sql = "UPDATE Airport SET name = ?, runwaySize = ?, "
                + "maxFixedWingParkingPlace = ?, maxRotatedWingParkingPlace = ? "
                + "WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, airport.getName());
            pstmt.setDouble(2, airport.getRunwaySize());
            pstmt.setInt(3, airport.getMaxFixedWingParkingPlace());
            pstmt.setInt(4, airport.getMaxRotatedWingParkingPlace());
            pstmt.setString(5, airport.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String airportId) {
        String sql = "DELETE FROM Airport WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, airportId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Airport getAirportById(String airportId) {
        String sql = "SELECT * FROM Airport WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, airportId);
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
    public List<Airport> getAllAirport() {
        String sql = "SELECT * FROM Airport";
        List<Airport> airports = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                airports.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return airports;
    }

    @Override
    public void createAirportSP(Airport airport) {
        String sql = "{call usp_CreateAirport(?, ?, ?, ?, ?)}";

        try (Connection conn = DBUtil.getConnection();
                CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setString(1, airport.getId());
            cstmt.setString(2, airport.getName());
            cstmt.setDouble(3, airport.getRunwaySize());
            cstmt.setInt(4, airport.getMaxFixedWingParkingPlace());
            cstmt.setInt(5, airport.getMaxRotatedWingParkingPlace());
            cstmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateAirportSP(Airport airport) {
        String sql = "{call usp_UpdateAirport(?, ?, ?, ?, ?)}";

        try (Connection conn = DBUtil.getConnection();
                CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setString(1, airport.getId());
            cstmt.setString(2, airport.getName());
            cstmt.setDouble(3, airport.getRunwaySize());
            cstmt.setInt(4, airport.getMaxFixedWingParkingPlace());
            cstmt.setInt(5, airport.getMaxRotatedWingParkingPlace());
            cstmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAirportSP(String airportId) {
        String sql = "{call usp_DeleteAirport(?)}";

        try (Connection conn = DBUtil.getConnection();
                CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setString(1, airportId);
            cstmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Tách riêng việc map ResultSet -> Airport để tránh lặp code
    private Airport mapRow(ResultSet rs) throws SQLException {
        return new Airport(
                rs.getString("id"),
                rs.getString("name"),
                rs.getDouble("runwaySize"),
                rs.getInt("maxFixedWingParkingPlace"),
                rs.getInt("maxRotatedWingParkingPlace"));
    }
}
