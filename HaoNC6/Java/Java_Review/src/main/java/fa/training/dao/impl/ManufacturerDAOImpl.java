package fa.training.dao.impl;

import fa.training.dao.ManufacturerDAO;
import fa.training.database.DBConnection;
import fa.training.entities.Manufacturer;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ManufacturerDAOImpl implements ManufacturerDAO {

    private static final String SP_INSERT = "CALL insert_manufacturer(?, ?, ?)";
    private static final String SP_UPDATE = "CALL update_manufacturer(?, ?, ?)";
    private static final String SP_DELETE = "CALL delete_manufacturer(?)";
    private static final String SP_GET_ALL = "{call get_all_manufacturers()}";
    private static final String SP_FIND_BY_ID = "{call find_manufacturer_by_id(?)}";

    @Override
    public List<Manufacturer> getAll() {
        List<Manufacturer> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(SP_GET_ALL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Manufacturer(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("country")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all manufacturers: " + e.getMessage());
        }
        return list;
    }

    @Override
    public Manufacturer findById(String id) {
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(SP_FIND_BY_ID)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Manufacturer(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getString("country")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding manufacturer by id: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean add(Manufacturer manufacturer) {
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(SP_INSERT)) {
            stmt.setString(1, manufacturer.getId());
            stmt.setString(2, manufacturer.getName());
            stmt.setString(3, manufacturer.getCountry());
            stmt.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding manufacturer: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Manufacturer manufacturer) {
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(SP_UPDATE)) {
            stmt.setString(1, manufacturer.getId());
            stmt.setString(2, manufacturer.getName());
            stmt.setString(3, manufacturer.getCountry());
            stmt.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error updating manufacturer: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(SP_DELETE)) {
            stmt.setString(1, id);
            stmt.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error deleting manufacturer: " + e.getMessage());
            return false;
        }
    }
}
