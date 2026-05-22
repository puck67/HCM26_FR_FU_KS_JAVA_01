package services;

import entities.User;
import utils.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    public UserService() {
    }

    public void addUser(User user) {
        String sql = "INSERT INTO Users (id, name) VALUES (?, ?)";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding user: " + e.getMessage());
        }
    }

    public List<User> getAllUser() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM Users ORDER BY id";
        try (Connection conn = DBContext.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new User(rs.getString("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching users: " + e.getMessage());
        }
        return list;
    }

    public User findUserById(String id) {
        String sql = "SELECT * FROM Users WHERE id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getString("id"), rs.getString("name"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding user: " + e.getMessage());
        }
        return null;
    }

    public boolean exists(String id) {
        return findUserById(id) != null;
    }

    public void displayAllUser() {
        List<User> userList = getAllUser();
        if (userList.isEmpty()) {
            System.out.println("  No users found.");
            return;
        }

        int idWidth = "User ID".length();
        int nameWidth = "User Name".length();
        for (User u : userList) {
            idWidth = Math.max(idWidth, u.getId().length());
            nameWidth = Math.max(nameWidth, u.getName().length());
        }
        String border = "+-" + "-".repeat(idWidth) + "-+-" + "-".repeat(nameWidth) + "-+";
        String fmt = "| %-" + idWidth + "s | %-" + nameWidth + "s |%n";
        System.out.println(border);
        System.out.printf(fmt, "User ID", "User Name");
        System.out.println(border);
        for (User user : userList) {
            System.out.printf(fmt, user.getId(), user.getName());
        }
        System.out.println(border);
        System.out.println("  Total: " + userList.size() + " user(s)");
    }

    public boolean updateUser(String id, String newName) {
        String sql = "UPDATE Users SET name = ? WHERE id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newName);
            ps.setString(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteUser(String id) {
        String sql = "DELETE FROM Users WHERE id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }
}
