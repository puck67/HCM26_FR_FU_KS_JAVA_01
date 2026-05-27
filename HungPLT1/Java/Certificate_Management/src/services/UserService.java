package services;

import entities.User;
import utils.Constants;
import utils.DBConnection;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {

    public boolean addUser(User user) {
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(Constants.SP_INSERT_USER)) {

            stmt.setString(1, user.getId());
            stmt.setString(2, user.getFullName());
            stmt.setString(3, user.getPhone());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getPassword());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User findById(String id) {
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(Constants.SP_FIND_USER_BY_ID)) {

            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getString("id"),
                            rs.getString("full_name"),
                            rs.getString("phone"),
                            rs.getString("email"),
                            rs.getString("password")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}