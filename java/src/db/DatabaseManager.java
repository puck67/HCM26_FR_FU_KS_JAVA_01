package db;

import util.ConsoleHelper;

import java.io.File;
import java.sql.*;


public class DatabaseManager {

    // ── Cấu hình kết nối ─────────────────────────────────────────────────────
    private static final String DB_DIR  = "data";
    /**
     * DB_CLOSE_ON_EXIT=FALSE: ngăn H2 đóng kết nối khi JVM shutdown hook chạy.
     * Giúp tránh lỗi "database already closed" khi gọi connection.close() thủ công.
     */
    private static final String DB_URL  =
            "jdbc:h2:file:./data/hotel_db;DB_CLOSE_ON_EXIT=FALSE";
    private static final String DB_USER = "sa";
    private static final String DB_PASS = "";

    // ── Singleton ─────────────────────────────────────────────────────────────
    private static DatabaseManager instance;
    private Connection connection;

    private DatabaseManager() {}

    /** Lấy instance duy nhất — thread-safe với synchronized. */
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) instance = new DatabaseManager();
        return instance;
    }

    // ── Public API ────────────────────────────────────────────────────────────

    /**
     * Khởi tạo toàn bộ DB: tạo thư mục, kết nối, tạo bảng,
     * đăng ký alias procedures, seed dữ liệu mẫu nếu trống.
     * Nếu thất bại → in lỗi và thoát ứng dụng.
     */
    public void init() {
        try {
            ensureDataDir();
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            createTable();
            createAliases();
            seedIfEmpty();
            ConsoleHelper.printSuccess(
                "H2 Database connected  →  " + DB_DIR + "/hotel_db.mv.db");
        } catch (SQLException e) {
            ConsoleHelper.printError("DB initialization failed: " + e.getMessage());
            System.exit(1);
        }
    }

    /** Khởi tạo database in-memory dành riêng cho Unit Test (không ghi đè file thật). */
    public void initForTest() {
        try {
            connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1", DB_USER, DB_PASS);
            createTable();
            createAliases();
            // Bỏ qua seedIfEmpty() để test case tự tạo dữ liệu
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khởi tạo DB Test", e);
        }
    }

    /** Trả về Connection hiện tại cho DAO sử dụng. */
    public Connection getConnection() { return connection; }

    /** Đóng kết nối khi người dùng chọn Exit. */
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

    // ── Private: Initialization Steps ────────────────────────────────────────

    /** Tạo thư mục data/ nếu chưa tồn tại. */
    private void ensureDataDir() {
        File dir = new File(DB_DIR);
        if (!dir.exists()) dir.mkdirs();
    }

    /**
     * Tạo bảng HOTEL_ROOM với 4 CHECK constraints tại tầng DB.
     * IF NOT EXISTS đảm bảo an toàn khi chạy lại.
     */
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
                    CONSTRAINT CHK_TYPE     CHECK (ROOM_TYPE IN
                                            ('SINGLE','DOUBLE','SUITE','DELUXE')),
                    CONSTRAINT CHK_STATUS   CHECK (STATUS IN
                                            ('AVAILABLE','OCCUPIED','MAINTENANCE'))
                )
                """;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(ddl);
        }
    }


    private void createAliases() throws SQLException {
        String[] aliases = {
            // CREATE
            "CREATE ALIAS IF NOT EXISTS SP_ADD_ROOM" +
            "       FOR \"db.H2Procedures.addRoom\"",
            // READ ALL
            "CREATE ALIAS IF NOT EXISTS SP_GET_ALL_ROOMS" +
            "  FOR \"db.H2Procedures.getAllRooms\"",
            // UPDATE
            "CREATE ALIAS IF NOT EXISTS SP_UPDATE_ROOM" +
            "    FOR \"db.H2Procedures.updateRoom\"",
            // DELETE
            "CREATE ALIAS IF NOT EXISTS SP_DELETE_ROOM" +
            "    FOR \"db.H2Procedures.deleteRoom\"",
            // SEARCH by ID
            "CREATE ALIAS IF NOT EXISTS SP_FIND_BY_ID" +
            "     FOR \"db.H2Procedures.findById\"",
            // SEARCH by Type
            "CREATE ALIAS IF NOT EXISTS SP_FIND_BY_TYPE" +
            "   FOR \"db.H2Procedures.findByType\"",
            // SEARCH by Status
            "CREATE ALIAS IF NOT EXISTS SP_FIND_BY_STATUS" +
            " FOR \"db.H2Procedures.findByStatus\"",
            // GENERATE ID
            "CREATE ALIAS IF NOT EXISTS SP_GENERATE_NEXT_ROOM_ID" +
            " FOR \"db.H2Procedures.generateNextRoomId\""
        };
        try (Statement stmt = connection.createStatement()) {
            for (String sql : aliases) stmt.execute(sql);
        }
    }

    /** Chèn 7 phòng mẫu nếu bảng HOTEL_ROOM đang trống,lần đầu chạy. */
    private void seedIfEmpty() throws SQLException {
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM HOTEL_ROOM")) {
            if (!rs.next() || rs.getInt(1) > 0) return;   // đã có data → bỏ qua
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
