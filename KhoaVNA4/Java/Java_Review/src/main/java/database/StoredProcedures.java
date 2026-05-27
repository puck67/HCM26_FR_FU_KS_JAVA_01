package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StoredProcedures {
    public static void addCat(Connection conn, String id, String name, String birthDate, String ownerId) throws SQLException {
        String sql = "INSERT INTO Cat (id, name, birth_date, owner_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.setString(2, name);
            stmt.setString(3, birthDate);
            stmt.setString(4, ownerId);
            stmt.executeUpdate();
        }
    }
    public static void deleteCat(Connection conn, String id) throws SQLException {
        String sql = "DELETE FROM Cat WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    public static void updateCat(Connection conn, String id, String name, String birthDate, String ownerId) throws SQLException {
        String sql = "UPDATE Cat SET name = ?, birth_date = ?, owner_id = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, birthDate);
            stmt.setString(3, ownerId);
            stmt.setString(4, id);
            stmt.executeUpdate();
        }
    }

    public static ResultSet getCatById(Connection conn, String id) throws SQLException {
        String sql = "SELECT * FROM Cat WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, id);
        return stmt.executeQuery();
    }

    public static ResultSet getAllCats(Connection conn) throws SQLException {
        String sql = "SELECT * FROM Cat";
        return conn.createStatement().executeQuery(sql);
    }

    public static void addOwner(Connection conn, String id, String name, String email, String phone) throws SQLException {
        String sql = "INSERT INTO Owner (id, name, email, phone) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.setString(2, name);
            stmt.setString(3, email);
            stmt.setString(4, phone);
            stmt.executeUpdate();
        }
    }

    public static void deleteOwner(Connection conn, String id) throws SQLException {
        String sql = "DELETE FROM Owner WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    public static void updateOwner(Connection conn, String id, String name, String email, String phone) throws SQLException {
        String sql = "UPDATE Owner SET name = ?, email = ?, phone = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            stmt.setString(4, id);
            stmt.executeUpdate();
        }
    }

    public static ResultSet getOwnerById(Connection conn, String id) throws SQLException {
        String sql = "SELECT * FROM Owner WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, id);
        return stmt.executeQuery();
    }

    public static ResultSet getAllOwners(Connection conn) throws SQLException {
        String sql = "SELECT * FROM Owner";
        return conn.createStatement().executeQuery(sql);
    }
}
