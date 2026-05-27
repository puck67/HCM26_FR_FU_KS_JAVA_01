package db;

import model.HotelRoom;
import model.RoomStatus;
import model.RoomType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HotelRoomDao {

    private Connection conn() {
        return DatabaseManager.getInstance().getConnection();
    }

    public void add(HotelRoom room) throws SQLException {
        try (PreparedStatement ps = conn().prepareStatement(
                "CALL SP_ADD_ROOM(?,?,?,?,?,?)")) {
            ps.setString(1, room.getRoomId());
            ps.setString(2, room.getRoomType().name());
            ps.setDouble(3, room.getPricePerNight());
            ps.setInt   (4, room.getCapacity());
            ps.setString(5, room.getStatus().name());
            ps.setString(6, room.getDescription());
            ps.execute();
        }
    }

    public List<HotelRoom> getAll() throws SQLException {
        List<HotelRoom> list = new ArrayList<>();
        try (PreparedStatement ps = conn().prepareStatement("CALL SP_GET_ALL_ROOMS()");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(toEntity(rs));
        }
        return list;
    }

    public Optional<HotelRoom> findById(String roomId) throws SQLException {
        try (PreparedStatement ps = conn().prepareStatement("CALL SP_FIND_BY_ID(?)")) {
            ps.setString(1, roomId.toUpperCase());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(toEntity(rs));
            }
        }
        return Optional.empty();
    }

    public List<HotelRoom> findByType(String roomType) throws SQLException {
        List<HotelRoom> list = new ArrayList<>();
        try (PreparedStatement ps = conn().prepareStatement("CALL SP_FIND_BY_TYPE(?)")) {
            ps.setString(1, roomType.toUpperCase());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(toEntity(rs));
            }
        }
        return list;
    }

    public List<HotelRoom> findByStatus(String status) throws SQLException {
        List<HotelRoom> list = new ArrayList<>();
        try (PreparedStatement ps = conn().prepareStatement("CALL SP_FIND_BY_STATUS(?)")) {
            ps.setString(1, status.toUpperCase());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(toEntity(rs));
            }
        }
        return list;
    }

    public boolean existsById(String roomId) throws SQLException {
        return findById(roomId).isPresent();
    }

    public String generateNextRoomId() throws SQLException {
        try (PreparedStatement ps = conn().prepareStatement("CALL SP_GENERATE_NEXT_ROOM_ID()");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getString(1);
            }
            return "R001";
        }
    }

    public void update(HotelRoom room) throws SQLException {
        try (PreparedStatement ps = conn().prepareStatement(
                "CALL SP_UPDATE_ROOM(?,?,?,?,?,?)")) {
            ps.setString(1, room.getRoomId());
            ps.setString(2, room.getRoomType().name());
            ps.setDouble(3, room.getPricePerNight());
            ps.setInt   (4, room.getCapacity());
            ps.setString(5, room.getStatus().name());
            ps.setString(6, room.getDescription());
            ps.execute();
        }
    }

    public void delete(String roomId) throws SQLException {
        try (PreparedStatement ps = conn().prepareStatement("CALL SP_DELETE_ROOM(?)")) {
            ps.setString(1, roomId.toUpperCase());
            ps.execute();
        }
    }

    private HotelRoom toEntity(ResultSet rs) throws SQLException {
        String typeStr   = rs.getString("ROOM_TYPE");
        String statusStr = rs.getString("STATUS");

        RoomType type = RoomType.fromString(typeStr)
                .orElseThrow(() -> new IllegalStateException(
                        "Invalid ROOM_TYPE in DB: " + typeStr));

        RoomStatus status = RoomStatus.fromString(statusStr)
                .orElseThrow(() -> new IllegalStateException(
                        "Invalid STATUS in DB: " + statusStr));

        return new HotelRoom(
            rs.getString("ROOM_ID"),
            type,
            rs.getDouble("PRICE_PER_NIGHT"),
            rs.getInt("CAPACITY"),
            status,
            rs.getString("DESCRIPTION")
        );
    }
}
