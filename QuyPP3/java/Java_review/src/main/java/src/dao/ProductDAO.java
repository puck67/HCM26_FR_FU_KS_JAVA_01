package src.dao;

import src.db.DBConnection;
import src.entities.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    private Connection conn() throws SQLException {
        return DBConnection.getConnection();
    }

    public boolean add(Product p) throws SQLException {
        try (CallableStatement stmt = conn().prepareCall("{call insert_product(?,?,?,?,?)}")) {
            stmt.setString(1, p.getId());
            stmt.setString(2, p.getName());
            stmt.setDouble(3, p.getPrice());
            stmt.setInt(4, p.getQuantity());
            stmt.setString(5, p.getCategory());
            stmt.execute();
            return true;
        }
    }

    public List<Product> getAll() throws SQLException {
        try (CallableStatement stmt = conn().prepareCall("{call get_all_products()}")) {
            ResultSet rs = stmt.executeQuery();
            List<Product> list = new ArrayList<>();
            while (rs.next()) list.add(mapRow(rs));
            return list;
        }
    }

    public boolean update(Product p) throws SQLException {
        try (CallableStatement stmt = conn().prepareCall("{call update_product(?,?,?,?,?)}")) {
            stmt.setString(1, p.getId());
            stmt.setString(2, p.getName());
            stmt.setDouble(3, p.getPrice());
            stmt.setInt(4, p.getQuantity());
            stmt.setString(5, p.getCategory());
            stmt.execute();
            return stmt.getUpdateCount() >= 0;
        }
    }

    public boolean delete(String id) throws SQLException {
        try (CallableStatement stmt = conn().prepareCall("{call delete_product(?)}")) {
            stmt.setString(1, id);
            stmt.execute();
            return stmt.getUpdateCount() > 0;
        }
    }

    public Product findById(String id) throws SQLException {
        try (CallableStatement stmt = conn().prepareCall("{call find_product_by_id(?)}")) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
            return null;
        }
    }

    private Product mapRow(ResultSet rs) throws SQLException {
        return new Product(
                rs.getString("id"),
                rs.getString("name"),
                rs.getDouble("price"),
                rs.getInt("quantity"),
                rs.getString("category")
        );
    }
}
