package src.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    private static final String URL = "jdbc:h2:./data/productdb;DB_CLOSE_DELAY=-1;AUTO_SERVER=TRUE";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("H2 JDBC Driver not found", e);
        }
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            initDatabase();
        }
        return connection;
    }

    private static void initDatabase() {
        try (Statement stmt = connection.createStatement()) {
            // 1. Create table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS Product (" +
                "id VARCHAR(10) PRIMARY KEY, " +
                "name VARCHAR(100) NOT NULL, " +
                "price DOUBLE NOT NULL, " +
                "quantity INT NOT NULL, " +
                "category VARCHAR(50) NOT NULL)"
            );

            // 2. SP: insert_product
            stmt.execute(
                "CREATE ALIAS IF NOT EXISTS insert_product AS $$\n" +
                "void insert_product(java.sql.Connection conn, String id, String name, double price, int qty, String cat) throws java.sql.SQLException {\n" +
                "    java.sql.PreparedStatement s = conn.prepareStatement(\"INSERT INTO Product(id,name,price,quantity,category) VALUES(?,?,?,?,?)\");\n" +
                "    s.setString(1,id); s.setString(2,name); s.setDouble(3,price); s.setInt(4,qty); s.setString(5,cat);\n" +
                "    s.executeUpdate(); s.close();\n" +
                "} $$"
            );

            // 3. SP: get_all_products
            stmt.execute(
                "CREATE ALIAS IF NOT EXISTS get_all_products AS $$\n" +
                "java.sql.ResultSet get_all_products(java.sql.Connection conn) throws java.sql.SQLException {\n" +
                "    return conn.createStatement().executeQuery(\"SELECT * FROM Product ORDER BY id\");\n" +
                "} $$"
            );

            // 4. SP: update_product
            stmt.execute(
                "CREATE ALIAS IF NOT EXISTS update_product AS $$\n" +
                "int update_product(java.sql.Connection conn, String id, String name, double price, int qty, String cat) throws java.sql.SQLException {\n" +
                "    java.sql.PreparedStatement s = conn.prepareStatement(\"UPDATE Product SET name=?,price=?,quantity=?,category=? WHERE id=?\");\n" +
                "    s.setString(1,name); s.setDouble(2,price); s.setInt(3,qty); s.setString(4,cat); s.setString(5,id);\n" +
                "    int rows = s.executeUpdate(); s.close(); return rows;\n" +
                "} $$"
            );

            // 5. SP: delete_product
            stmt.execute(
                "CREATE ALIAS IF NOT EXISTS delete_product AS $$\n" +
                "int delete_product(java.sql.Connection conn, String id) throws java.sql.SQLException {\n" +
                "    java.sql.PreparedStatement s = conn.prepareStatement(\"DELETE FROM Product WHERE id=?\");\n" +
                "    s.setString(1,id);\n" +
                "    int rows = s.executeUpdate(); s.close(); return rows;\n" +
                "} $$"
            );

            // 6. SP: find_product_by_id
            stmt.execute(
                "CREATE ALIAS IF NOT EXISTS find_product_by_id AS $$\n" +
                "java.sql.ResultSet find_product_by_id(java.sql.Connection conn, String id) throws java.sql.SQLException {\n" +
                "    java.sql.PreparedStatement s = conn.prepareStatement(\"SELECT * FROM Product WHERE id=?\");\n" +
                "    s.setString(1,id);\n" +
                "    return s.executeQuery();\n" +
                "} $$"
            );

        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}
