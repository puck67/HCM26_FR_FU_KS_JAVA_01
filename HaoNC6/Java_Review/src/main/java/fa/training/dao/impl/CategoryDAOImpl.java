package fa.training.dao.impl;

import fa.training.dao.CategoryDAO;
import fa.training.database.DBConnection;
import fa.training.entities.Category;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAOImpl implements CategoryDAO {

    private static final String SP_INSERT = "CALL insert_category(?, ?, ?)";
    private static final String SP_UPDATE = "CALL update_category(?, ?, ?)";
    private static final String SP_DELETE = "CALL delete_category(?)";
    private static final String SP_GET_ALL = "{call get_all_categories()}";
    private static final String SP_FIND_BY_ID = "{call find_category_by_id(?)}";

    @Override
    public List<Category> getAll() {
        List<Category> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(SP_GET_ALL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Category(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all categories: " + e.getMessage());
        }
        return list;
    }

    @Override
    public Category findById(String id) {
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(SP_FIND_BY_ID)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Category(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getString("description")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding category by id: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean add(Category category) {
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(SP_INSERT)) {
            stmt.setString(1, category.getId());
            stmt.setString(2, category.getName());
            stmt.setString(3, category.getDescription());
            stmt.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding category: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Category category) {
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(SP_UPDATE)) {
            stmt.setString(1, category.getId());
            stmt.setString(2, category.getName());
            stmt.setString(3, category.getDescription());
            stmt.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error updating category: " + e.getMessage());
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
            System.err.println("Error deleting category: " + e.getMessage());
            return false;
        }
    }
}
