package fa.training.dao.impl;

import fa.training.dao.ShoppingItemDAO;
import fa.training.db.DBConnection;
import fa.training.entity.ShoppingItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShoppingItemDAOImpl
        implements ShoppingItemDAO {

    @Override
    public boolean add(ShoppingItem item) {

        String sql =
                "{call insert_shopping_item(?,?,?,?,?)}";

        try (
                Connection conn =
                        DBConnection.getConnection();

                CallableStatement stmt =
                        conn.prepareCall(sql)
        ) {

            stmt.setString(1, item.getId());
            stmt.setString(2, item.getItemName());
            stmt.setString(3, item.getCategory());
            stmt.setDouble(4, item.getPrice());
            stmt.setInt(5, item.getQuantity());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public List<ShoppingItem> getAll() {

        List<ShoppingItem> list =
                new ArrayList<>();

        String sql =
                "{call get_all_shopping_items()}";

        try (
                Connection conn =
                        DBConnection.getConnection();

                CallableStatement stmt =
                        conn.prepareCall(sql);

                ResultSet rs =
                        stmt.executeQuery()
        ) {

            while (rs.next()) {

                ShoppingItem item =
                        new ShoppingItem(
                                rs.getString("id"),
                                rs.getString("item_name"),
                                rs.getString("category"),
                                rs.getDouble("price"),
                                rs.getInt("quantity")
                        );

                list.add(item);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public boolean update(ShoppingItem item) {

        String sql =
                "{call update_shopping_item(?,?,?,?,?)}";

        try (
                Connection conn =
                        DBConnection.getConnection();

                CallableStatement stmt =
                        conn.prepareCall(sql)
        ) {

            stmt.setString(1, item.getId());
            stmt.setString(2, item.getItemName());
            stmt.setString(3, item.getCategory());
            stmt.setDouble(4, item.getPrice());
            stmt.setInt(5, item.getQuantity());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean delete(String id) {

        String sql =
                "{call delete_shopping_item(?)}";

        try (
                Connection conn =
                        DBConnection.getConnection();

                CallableStatement stmt =
                        conn.prepareCall(sql)
        ) {

            stmt.setString(1, id);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public ShoppingItem findById(String id) {

        String sql =
                "{call find_shopping_item_by_id(?)}";

        try (
                Connection conn =
                        DBConnection.getConnection();

                CallableStatement stmt =
                        conn.prepareCall(sql)
        ) {

            stmt.setString(1, id);

            ResultSet rs =
                    stmt.executeQuery();

            if (rs.next()) {

                return new ShoppingItem(
                        rs.getString("id"),
                        rs.getString("item_name"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}