package src.dao;

import src.db.DBConnection;
import src.entities.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public boolean add(Product p) throws SQLException {
        Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO Product(id, name, price, quantity, category) VALUES (?, ?, ?, ?, ?)"
        );
        stmt.setString(1, p.getId());
        stmt.setString(2, p.getName());
        stmt.setDouble(3, p.getPrice());
        stmt.setInt(4, p.getQuantity());
        stmt.setString(5, p.getCategory());
        int rows = stmt.executeUpdate();
        stmt.close();
        return rows > 0;
    }

    public List<Product> getAll() throws SQLException {
        Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Product ORDER BY id");
        ResultSet rs = stmt.executeQuery();
        List<Product> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapRow(rs));
        }
        stmt.close();
        return list;
    }

    public boolean update(Product p) throws SQLException {
        Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
                "UPDATE Product SET name=?, price=?, quantity=?, category=? WHERE id=?"
        );
        stmt.setString(1, p.getName());
        stmt.setDouble(2, p.getPrice());
        stmt.setInt(3, p.getQuantity());
        stmt.setString(4, p.getCategory());
        stmt.setString(5, p.getId());
        int rows = stmt.executeUpdate();
        stmt.close();
        return rows > 0;
    }

    public boolean delete(String id) throws SQLException {
        Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Product WHERE id=?");
        stmt.setString(1, id);
        int rows = stmt.executeUpdate();
        stmt.close();
        return rows > 0;
    }

    public Product findById(String id) throws SQLException {
        Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Product WHERE id=?");
        stmt.setString(1, id);
        ResultSet rs = stmt.executeQuery();
        Product p = null;
        if (rs.next()) {
            p = mapRow(rs);
        }
        stmt.close();
        return p;
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
