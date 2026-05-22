package database;

import common.ResultSetMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBConnection {
    private static final String JDBC_URL = "jdbc:h2:./data/studentdb;AUTO_SERVER=TRUE";
    private static final String USER = "sa";
    private static final String PASSWORD = "";
    static {
        try {
            Class.forName("org.h2.Driver");
            initializeDatabase();
        } catch (ClassNotFoundException e) {
            System.err.println("H2 JDBC Driver not found: " + e.getMessage());
        }
    }
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }
    private static void initializeDatabase() {
        String createTableOwner = "CREATE TABLE IF NOT EXISTS Owner (" +
                "id VARCHAR(10) NOT NULL PRIMARY KEY, " +
                "name VARCHAR(100) NOT NULL, " +
                "email VARCHAR(100) NOT NULL, " +
                "phone VARCHAR(100) NOT NULL" +
                ");";
        String createTableCat = "CREATE TABLE IF NOT EXISTS Cat (" +
                "id VARCHAR(10) NOT NULL PRIMARY KEY, " +
                "name VARCHAR(100) NOT NULL, " +
                "birth_date VARCHAR(100) NOT NULL, " +
                "owner_id VARCHAR(10) NOT NULL, " +
                "FOREIGN KEY (owner_id) REFERENCES Owner(id)" +
                ");";
        String aliasAddCat = "CREATE ALIAS IF NOT EXISTS addCat FOR \"database.StoredProcedures.addCat\";";
        String aliasDeleteCat = "CREATE ALIAS IF NOT EXISTS deleteCat FOR \"database.StoredProcedures.deleteCat\";";
        String aliasUpdateCat = "CREATE ALIAS IF NOT EXISTS updateCat FOR \"database.StoredProcedures.updateCat\";";
        String aliasGetCatById = "CREATE ALIAS IF NOT EXISTS getCatById FOR \"database.StoredProcedures.getCatById\";";
        String aliasGetAllCats = "CREATE ALIAS IF NOT EXISTS getAllCats FOR \"database.StoredProcedures.getAllCats\";";

        String aliasAddOwner = "CREATE ALIAS IF NOT EXISTS addOwner FOR \"database.StoredProcedures.addOwner\";";
        String aliasDeleteOwner = "CREATE ALIAS IF NOT EXISTS deleteOwner FOR \"database.StoredProcedures.deleteOwner\";";
        String aliasUpdateOwner = "CREATE ALIAS IF NOT EXISTS updateOwner FOR \"database.StoredProcedures.updateOwner\";";
        String aliasGetOwnerById = "CREATE ALIAS IF NOT EXISTS getOwnerById FOR \"database.StoredProcedures.getOwnerById\";";
        String aliasGetAllOwners = "CREATE ALIAS IF NOT EXISTS getAllOwners FOR \"database.StoredProcedures.getAllOwners\";";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableOwner);
            stmt.execute(createTableCat);
            stmt.execute(aliasAddCat);
            stmt.execute(aliasDeleteCat);
            stmt.execute(aliasUpdateCat);
            stmt.execute(aliasGetCatById);
            stmt.execute(aliasGetAllCats);
            stmt.execute(aliasAddOwner);
            stmt.execute(aliasDeleteOwner);
            stmt.execute(aliasUpdateOwner);
            stmt.execute(aliasGetOwnerById);
            stmt.execute(aliasGetAllOwners);
            System.out.println("H2 Database initialized successfully with Stored Procedures.");
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }
    public static <T> List<T> executeQuery(String sql, ResultSetMapper<T> mapper, Object... params) {
        List<T> results = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            setParameters(stmt, params);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapper.map(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error executing Query: " + e.getMessage());
            e.printStackTrace();
        }
        return results;
    }

    public static boolean executeUpdate(String sql, Object... params) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            setParameters(stmt, params);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error executing Update: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean executeProcedureUpdate(String procSql, Object... params) {
        try (Connection conn = getConnection();
             CallableStatement stmt = conn.prepareCall(procSql)) {
            setParameters(stmt, params);
            return stmt.executeUpdate() >= 0;
        } catch (SQLException e) {
            System.err.println("Error executing Procedure Update: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static <T> List<T> executeProcedureQuery(String procSql, ResultSetMapper<T> mapper, Object... params) {
        List<T> results = new ArrayList<>();
        try (Connection conn = getConnection();
             CallableStatement stmt = conn.prepareCall(procSql)) {
            setParameters(stmt, params);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapper.map(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error executing Procedure Query: " + e.getMessage());
            e.printStackTrace();
        }
        return results;
    }
    private static void setParameters(PreparedStatement stmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];
            if (param instanceof java.util.Date && !(param instanceof java.sql.Date)
                    && !(param instanceof java.sql.Timestamp)) {
                stmt.setTimestamp(i + 1, new java.sql.Timestamp(((java.util.Date) param).getTime()));
            } else {
                stmt.setObject(i + 1, param);
            }
        }
    }
}
