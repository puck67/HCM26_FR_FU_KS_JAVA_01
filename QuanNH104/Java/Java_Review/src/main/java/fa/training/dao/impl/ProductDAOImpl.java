package fa.training.dao.impl;

import fa.training.dao.ProductDAO;
import fa.training.db.DBConnection;
import fa.training.model.Product;
import fa.training.validation.Validator;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImpl implements ProductDAO {

    @Override
    public boolean add(Product product) throws SQLException {
        // Validate input data
        Validator.validateProduct(product.getId(), product.getName(), product.getPrice(), product.getQuantity());

        if (product.getWarehouseId() == null) {
            throw new IllegalArgumentException("Product must be assigned to a warehouse.");
        }

        // Check if product ID already exists
        if (findById(product.getId()) != null) {
            throw new IllegalArgumentException("Product ID '" + product.getId() + "' already exists.");
        }

        String sql = "{call insert_product(?, ?, ?, ?, ?)}";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setString(1, product.getId());
            stmt.setString(2, product.getName());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getQuantity());
            stmt.setString(5, product.getWarehouseId());
            
            stmt.execute();
            return true;
        }
    }

    @Override
    public List<Product> getByWarehouseId(String warehouseId) throws SQLException {
        List<Product> list = new ArrayList<>();
        String sql = "{call get_products_by_warehouse(?)}";
        
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setString(1, warehouseId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    var prod = new Product(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        rs.getString("warehouse_id")
                    );
                    list.add(prod);
                }
            }
        }
        return list;
    }

    @Override
    public Product findById(String id) throws SQLException {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }

        String sql = "SELECT * FROM products WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Product(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        rs.getString("warehouse_id")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public int getTotalQuantityByWarehouseId(String warehouseId) throws SQLException {
        String sql = "{? = call get_total_quantity_in_warehouse(?)}";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.setString(2, warehouseId);
            stmt.execute();
            
            return stmt.getInt(1);
        }
    }
}
