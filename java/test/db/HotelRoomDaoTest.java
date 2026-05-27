package db;

import model.HotelRoom;
import model.RoomStatus;
import model.RoomType;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HotelRoomDaoTest {

    private HotelRoomDao dao;

    @BeforeAll
    void setupDatabase() {
        DatabaseManager.getInstance().initForTest();
        dao = new HotelRoomDao();
    }

    @AfterEach
    void cleanUpTable() throws SQLException {
        Connection conn = DatabaseManager.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM HOTEL_ROOM")) {
            ps.executeUpdate();
        }
    }

    @Test
    @DisplayName("Thêm phòng thành công")
    void testAddRoom() throws SQLException {
        HotelRoom room = new HotelRoom("R101", RoomType.SINGLE, 100.0, 1, RoomStatus.AVAILABLE, "Test room");
        dao.add(room);

        assertTrue(dao.existsById("R101"), "Room nên tồn tại sau khi add");
        Optional<HotelRoom> fetched = dao.findById("R101");
        assertTrue(fetched.isPresent());
        assertEquals("Test room", fetched.get().getDescription());
    }

    @Test
    @DisplayName("Lấy phòng theo ID (Đã add trước khi get)")
    void testGetRoom() throws SQLException {
        HotelRoom room = new HotelRoom("R102", RoomType.DOUBLE, 150.0, 2, RoomStatus.OCCUPIED, "Double test");
        dao.add(room);
        Optional<HotelRoom> fetched = dao.findById("R102");
        assertTrue(fetched.isPresent(), "Phải lấy ra được phòng vừa add");
        assertEquals("R102", fetched.get().getRoomId());
        assertEquals(RoomType.DOUBLE, fetched.get().getRoomType());
    }

    @Test
    @DisplayName("Cập nhật phòng (Đã add trước khi update)")
    void testUpdateRoom() throws SQLException {
        HotelRoom room = new HotelRoom("R103", RoomType.SUITE, 300.0, 4, RoomStatus.AVAILABLE, "Suite test");
        dao.add(room);
        room.setPricePerNight(350.0);
        room.setStatus(RoomStatus.MAINTENANCE);
        dao.update(room);
        Optional<HotelRoom> updated = dao.findById("R103");
        assertTrue(updated.isPresent());
        assertEquals(350.0, updated.get().getPricePerNight());
        assertEquals(RoomStatus.MAINTENANCE, updated.get().getStatus());
    }

    @Test
    @DisplayName("Xóa phòng (Đã add trước khi delete)")
    void testDeleteRoom() throws SQLException {
        HotelRoom room = new HotelRoom("R104", RoomType.DELUXE, 250.0, 3, RoomStatus.AVAILABLE, "Deluxe test");
        dao.add(room);
        assertTrue(dao.existsById("R104"));
        dao.delete("R104");
        assertFalse(dao.existsById("R104"), "Phòng phải không còn tồn tại sau khi xóa");
    }
}
