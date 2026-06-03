package fa.training.dao.impl;

import fa.training.dao.VehicleDAO;
import fa.training.dao.ManufacturerDAO;
import fa.training.dao.CategoryDAO;
import fa.training.database.DBConnection;
import fa.training.entities.Manufacturer;
import fa.training.entities.Category;
import fa.training.entities.Vehicle;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAOImpl implements VehicleDAO {

    private static final String SP_INSERT = "CALL insert_vehicle(?, ?, ?, ?, ?, ?, ?)";
    private static final String SP_UPDATE = "CALL update_vehicle(?, ?, ?, ?, ?, ?, ?)";
    private static final String SP_DELETE = "CALL delete_vehicle(?)";
    private static final String SP_GET_ALL = "{call get_all_vehicles()}";
    private static final String SP_FIND_BY_ID = "{call find_vehicle_by_id(?)}";

    private final ManufacturerDAO manufacturerDAO = new ManufacturerDAOImpl();
    private final CategoryDAO categoryDAO = new CategoryDAOImpl();

    @Override
    public boolean add(Vehicle vehicle) {
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(SP_INSERT)) {
            stmt.setString(1, vehicle.getId());
            stmt.setString(2, vehicle.getModel());
            stmt.setDouble(3, vehicle.getPrice());
            stmt.setString(4, vehicle.getOwnerEmail());
            stmt.setString(5, vehicle.getOwnerPhone());
            stmt.setString(6, vehicle.getManufacturer() != null ? vehicle.getManufacturer().getId() : null);
            stmt.setString(7, vehicle.getCategory() != null ? vehicle.getCategory().getId() : null);
            stmt.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding vehicle: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Vehicle> getAll() {
        List<Vehicle> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(SP_GET_ALL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all vehicles: " + e.getMessage());
        }
        return list;
    }

    @Override
    public boolean update(Vehicle vehicle) {
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(SP_UPDATE)) {
            stmt.setString(1, vehicle.getId());
            stmt.setString(2, vehicle.getModel());
            stmt.setDouble(3, vehicle.getPrice());
            stmt.setString(4, vehicle.getOwnerEmail());
            stmt.setString(5, vehicle.getOwnerPhone());
            stmt.setString(6, vehicle.getManufacturer() != null ? vehicle.getManufacturer().getId() : null);
            stmt.setString(7, vehicle.getCategory() != null ? vehicle.getCategory().getId() : null);
            stmt.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error updating vehicle: " + e.getMessage());
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
            System.err.println("Error deleting vehicle: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Vehicle findById(String id) {
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(SP_FIND_BY_ID)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding vehicle by id: " + e.getMessage());
        }
        return null;
    }

    private Vehicle mapRow(ResultSet rs) throws SQLException {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(rs.getString("id"));
        vehicle.setModel(rs.getString("model"));
        vehicle.setPrice(rs.getDouble("price"));
        vehicle.setOwnerEmail(rs.getString("owner_email"));
        vehicle.setOwnerPhone(rs.getString("owner_phone"));

        String mId = rs.getString("manufacturer_id");
        if (mId != null) {
            vehicle.setManufacturer(manufacturerDAO.findById(mId));
        }

        String cId = rs.getString("category_id");
        if (cId != null) {
            vehicle.setCategory(categoryDAO.findById(cId));
        }

        return vehicle;
    }
}
