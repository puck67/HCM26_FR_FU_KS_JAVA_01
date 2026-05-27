package db;

import util.ConsoleHelper;

import java.io.File;
import java.sql.*;

public class DatabaseManager {

    private static final String DB_DIR  = "data";
    private static final String DB_URL  = "jdbc:h2:file:./data/hotel_db;DB_CLOSE_ON_EXIT=FALSE";
    private static final String DB_USER = "sa";
    private static final String DB_PASS = "";

    private static DatabaseManager instance;
    private Connection connection;

    private DatabaseManager() {}

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) instance = new DatabaseManager();
        return instance;
    }

    public void init() {
        try {
            ensureDataDir();
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            createTable();
            createAliases();
            initData();
            ConsoleHelper.printSuccess("H2 Database connected  →  " + DB_DIR + "/hotel_db.mv.db");
        } catch (SQLException e) {
            ConsoleHelper.printError("DB initialization failed: " + e.getMessage());
            System.exit(1);
        }
    }

    public void initForTest() {
        try {
            connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1", DB_USER, DB_PASS);
            createTable();
            createAliases();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error initializing test DB", e);
        }
    }

    public Connection getConnection() { return connection; }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                ConsoleHelper.printInfo("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void ensureDataDir() {
        File dir = new File(DB_DIR);
        if (!dir.exists()) dir.mkdirs();
    }

    private void createTable() throws SQLException {
        String ddl = """
                CREATE TABLE IF NOT EXISTS HOTEL_ROOM (
                    ROOM_ID          VARCHAR(10)  NOT NULL,
                    ROOM_TYPE        VARCHAR(20)  NOT NULL,
                    PRICE_PER_NIGHT  DOUBLE       NOT NULL,
                    CAPACITY         INT          NOT NULL,
                    STATUS           VARCHAR(20)  NOT NULL,
                    DESCRIPTION      VARCHAR(200) NOT NULL,
                    CONSTRAINT PK_ROOM      PRIMARY KEY (ROOM_ID),
                    CONSTRAINT CHK_PRICE    CHECK (PRICE_PER_NIGHT > 0),
                    CONSTRAINT CHK_CAPACITY CHECK (CAPACITY BETWEEN 1 AND 10),
                    CONSTRAINT CHK_TYPE     CHECK (ROOM_TYPE IN ('SINGLE','DOUBLE','SUITE','DELUXE')),
                    CONSTRAINT CHK_STATUS   CHECK (STATUS IN ('AVAILABLE','OCCUPIED','MAINTENANCE'))
                )
                """;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(ddl);
        }
    }

    private void createAliases() throws SQLException {
        String[] aliases = {
            "CREATE ALIAS IF NOT EXISTS SP_ADD_ROOM FOR \"db.H2Procedures.addRoom\"",
            "CREATE ALIAS IF NOT EXISTS SP_GET_ALL_ROOMS FOR \"db.H2Procedures.getAllRooms\"",
            "CREATE ALIAS IF NOT EXISTS SP_UPDATE_ROOM FOR \"db.H2Procedures.updateRoom\"",
            "CREATE ALIAS IF NOT EXISTS SP_DELETE_ROOM FOR \"db.H2Procedures.deleteRoom\"",
            "CREATE ALIAS IF NOT EXISTS SP_FIND_BY_ID FOR \"db.H2Procedures.findById\"",
            "CREATE ALIAS IF NOT EXISTS SP_FIND_BY_TYPE FOR \"db.H2Procedures.findByType\"",
            "CREATE ALIAS IF NOT EXISTS SP_FIND_BY_STATUS FOR \"db.H2Procedures.findByStatus\"",
            "CREATE ALIAS IF NOT EXISTS SP_GENERATE_NEXT_ROOM_ID FOR \"db.H2Procedures.generateNextRoomId\""
        };
        try (Statement stmt = connection.createStatement()) {
            for (String sql : aliases) stmt.execute(sql);
        }
    }

    private void initData() throws SQLException {
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM HOTEL_ROOM")) {
            if (!rs.next() || rs.getInt(1) > 0) return;
        }
        Object[][] rows = {
            {"R001", "SINGLE",  89.99,  1, "AVAILABLE",   "Cozy single room with city view"},
            {"R002", "DOUBLE", 129.99,  2, "OCCUPIED",    "Spacious double room, sea view"},
            {"R003", "SUITE",  299.99,  4, "AVAILABLE",   "Luxury suite with private balcony"},
            {"R004", "DELUXE", 199.99,  3, "MAINTENANCE", "Deluxe room under renovation"},
            {"R005", "SINGLE",  75.00,  1, "AVAILABLE",   "Budget single, garden view"},
            {"R006", "DOUBLE", 149.99,  2, "OCCUPIED",    "Double room with king bed"},
            {"R007", "SUITE",  399.99,  6, "AVAILABLE",   "Presidential suite, full ocean view"}
        };
        String sql = "INSERT INTO HOTEL_ROOM VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (Object[] row : rows) {
                ps.setString(1, (String)  row[0]);
                ps.setString(2, (String)  row[1]);
                ps.setDouble(3, (Double)  row[2]);
                ps.setInt   (4, (Integer) row[3]);
                ps.setString(5, (String)  row[4]);
                ps.setString(6, (String)  row[5]);
                ps.addBatch();
            }
            ps.executeBatch();
        }
        ConsoleHelper.printInfo("Sample data seeded: 7 rooms inserted.");
    }
}
