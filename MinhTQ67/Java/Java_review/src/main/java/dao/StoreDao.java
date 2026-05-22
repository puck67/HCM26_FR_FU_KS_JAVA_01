package dao;

import database.DBConnection;
import model.Store;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StoreDao {
    private final StoreValidator validator = new StoreValidator();

    public StoreDao() {
        initTable();
    }

    private void initTable() {
        String sql = "CREATE TABLE IF NOT EXISTS stores (" +
                     "id VARCHAR(10) PRIMARY KEY, " +
                     "name VARCHAR(100), " +
                     "location VARCHAR(200), " +
                     "phone VARCHAR(20), " +
                     "rating DOUBLE)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
        } catch (SQLException e) {
            System.err.println("Error initializing table: " + e.getMessage());
        }
    }


    public boolean add(Store store) {
        String validationError = validator.validateStore(store);
        if (validationError != null) {
            System.err.println("Validation error: " + validationError);
            return false;
        }
        
        String sql = "INSERT INTO stores (id, name, location, phone, rating) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, store.getId());
            stmt.setString(2, store.getName());
            stmt.setString(3, store.getLocation());
            stmt.setString(4, store.getPhone());
            stmt.setDouble(5, store.getRating());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error adding store: " + e.getMessage());
            return false;
        }
    }

    public List<Store> getAll() {
        List<Store> stores = new ArrayList<>();
        String sql = "SELECT * FROM stores";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                stores.add(new Store(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("location"),
                        rs.getString("phone"),
                        rs.getDouble("rating")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all stores: " + e.getMessage());
        }
        return stores;
    }

    public boolean update(Store store) {
        String validationError = validator.validateStore(store);
        if (validationError != null) {
            System.err.println("Validation error: " + validationError);
            return false;
        }
        
        String sql = "UPDATE stores SET name = ?, location = ?, phone = ?, rating = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, store.getName());
            stmt.setString(2, store.getLocation());
            stmt.setString(3, store.getPhone());
            stmt.setDouble(4, store.getRating());
            stmt.setString(5, store.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating store: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(String id) {
        String sql = "DELETE FROM stores WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting store: " + e.getMessage());
            return false;
        }
    }

    public Store findById(String id) {
        String sql = "SELECT * FROM stores WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Store(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getString("location"),
                            rs.getString("phone"),
                            rs.getDouble("rating")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding store: " + e.getMessage());
        }
        return null;
    }

    public static class StoreValidator {

        public String validateStore(Store store) {
            if (store == null) {
                return "Store object is null";
            }

            // Validate ID
            String id = store.getId();
            if (id == null || id.trim().isEmpty()) {
                return "Store ID cannot be empty";
            }
            if (id.trim().length() > 10) {
                return "Store ID cannot exceed 10 characters";
            }

            // Validate Name
            String name = store.getName();
            if (name == null || name.trim().isEmpty()) {
                return "Store Name cannot be empty";
            }
            if (name.trim().length() > 100) {
                return "Store Name cannot exceed 100 characters";
            }

            // Validate Location
            String location = store.getLocation();
            if (location != null && location.length() > 200) {
                return "Location cannot exceed 200 characters";
            }

            // Validate Phone
            String phone = store.getPhone();
            if (phone != null && !phone.isEmpty()) {
                if (!isValidPhone(phone)) {
                    return "Phone number format is invalid (must be digits, +, -, or space)";
                }
                if (phone.length() > 20) {
                    return "Phone cannot exceed 20 characters";
                }
            }

            // Validate Rating
            double rating = store.getRating();
            if (rating < 0 || rating > 5) {
                return "Rating must be between 0 and 5";
            }

            return null; // No errors
        }

        // Validate individual fields for scanner input
        public String validateId(String id) {
            if (id == null || id.trim().isEmpty()) {
                return "Store ID cannot be empty";
            }
            if (id.trim().length() > 10) {
                return "Store ID cannot exceed 10 characters";
            }
            return null;
        }

        public String validateName(String name) {
            if (name == null || name.trim().isEmpty()) {
                return "Store Name cannot be empty";
            }
            if (name.trim().length() > 100) {
                return "Store Name cannot exceed 100 characters";
            }
            return null;
        }

        public String validateLocation(String location) {
            if (location != null && location.length() > 200) {
                return "Location cannot exceed 200 characters";
            }
            return null;
        }

        public String validatePhone(String phone) {
            if (phone != null && !phone.isEmpty()) {
                if (!isValidPhone(phone)) {
                    return "Phone number format is invalid (must be digits, +, -, or space)";
                }
                if (phone.length() > 20) {
                    return "Phone cannot exceed 20 characters";
                }
            }
            return null;
        }

        public String validateRating(double rating) {
            if (rating < 0 || rating > 5) {
                return "Rating must be between 0 and 5";
            }
            return null;
        }

        private boolean isValidPhone(String phone) {
            // Allow phone numbers with digits, +, -, (), and spaces
            // Must contain at least one digit
            return phone.matches("^[\\d+\\-\\s()]*\\d[\\d+\\-\\s()]*$");
        }
    }
}

