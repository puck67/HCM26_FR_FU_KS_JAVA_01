package db;

import java.sql.*;

public class H2Procedures {

    public static void addRoom(Connection conn,
                               String roomId,   String roomType,
                               double price,    int    capacity,
                               String status,   String description) throws SQLException {
        String sql = """
                INSERT INTO HOTEL_ROOM
                    (ROOM_ID, ROOM_TYPE, PRICE_PER_NIGHT, CAPACITY, STATUS, DESCRIPTION)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roomId);
            ps.setString(2, roomType.toUpperCase());
            ps.setDouble(3, price);
            ps.setInt   (4, capacity);
            ps.setString(5, status.toUpperCase());
            ps.setString(6, description);
            ps.executeUpdate();
        }
    }

    public static ResultSet getAllRooms(Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM HOTEL_ROOM ORDER BY ROOM_ID");
        return ps.executeQuery();
    }

    public static void updateRoom(Connection conn,
                                  String roomId,   String roomType,
                                  double price,    int    capacity,
                                  String status,   String description) throws SQLException {
        String sql = """
                UPDATE HOTEL_ROOM SET
                    ROOM_TYPE        = ?,
                    PRICE_PER_NIGHT  = ?,
                    CAPACITY         = ?,
                    STATUS           = ?,
                    DESCRIPTION      = ?
                WHERE ROOM_ID = ?
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roomType.toUpperCase());
            ps.setDouble(2, price);
            ps.setInt   (3, capacity);
            ps.setString(4, status.toUpperCase());
            ps.setString(5, description);
            ps.setString(6, roomId);
            ps.executeUpdate();
        }
    }

    public static void deleteRoom(Connection conn, String roomId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM HOTEL_ROOM WHERE ROOM_ID = ?")) {
            ps.setString(1, roomId == null ? "" : roomId.toUpperCase());
            ps.executeUpdate();
        }
    }

    public static ResultSet findById(Connection conn, String roomId) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM HOTEL_ROOM WHERE ROOM_ID = ?");
        ps.setString(1, roomId == null ? "" : roomId.toUpperCase());
        return ps.executeQuery();
    }

    public static ResultSet findByType(Connection conn, String roomType) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM HOTEL_ROOM WHERE ROOM_TYPE = ? ORDER BY ROOM_ID");
        ps.setString(1, roomType == null ? "" : roomType.toUpperCase());
        return ps.executeQuery();
    }

    public static ResultSet findByStatus(Connection conn, String status) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM HOTEL_ROOM WHERE STATUS = ? ORDER BY ROOM_ID");
        ps.setString(1, status == null ? "" : status.toUpperCase());
        return ps.executeQuery();
    }

    public static String generateNextRoomId(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT MAX(CAST(SUBSTRING(ROOM_ID, 2) AS INT)) FROM HOTEL_ROOM")) {
            if (rs.next()) {
                int max = rs.getInt(1);
                return String.format("R%03d", max + 1);
            }
            return "R001";
        }
    }
}
