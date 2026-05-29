package fa.training.dao;

import fa.training.dao.impl.RoomDaoImpl;
import fa.training.dao.impl.SeatDaoImpl;
import fa.training.entities.CinemaRoom;
import fa.training.entities.Seat;
import fa.training.utils.HibernateUtils;
import org.junit.jupiter.api.*;
import java.util.List;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.*;

public class SeatDaoTest {

    private static RoomDao roomDao;
    private static SeatDao seatDao;
    private CinemaRoom testRoom;

    @BeforeAll
    public static void setUpClass() {
        roomDao = new RoomDaoImpl();
        seatDao = new SeatDaoImpl();
    }

    @AfterAll
    public static void tearDownClass() {
        HibernateUtils.shutdown();
    }

    @BeforeEach
    public void cleanDatabase() {
        List<CinemaRoom> rooms = roomDao.getAllRoom();
        for (CinemaRoom r : rooms) {
            roomDao.deleteRoomById(r.getCinemaRoomId());
        }

        testRoom = new CinemaRoom("Test Cinema Room A", 50);
        roomDao.insertRoom(testRoom);
    }

    @Test
    public void testInsertSeat_Success() {
        Seat seat = new Seat("C", 1, "Available", "VIP");
        seat.setCinemaRoom(testRoom);

        boolean result = seatDao.insertSeat(seat);

        assertTrue(result, new StringBuilder()
                .append("Failed to insert seat. Column: ")
                .append(seat.getSeatColumn())
                .toString());
        assertNotNull(seat.getSeatId());
    }

    @Test
    public void testInsertSeat_InvalidStatus_ShouldFail() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Seat("C", 2, "Broken", "Normal");
        }, new StringBuilder()
                .append("Should throw IllegalArgumentException for invalid seat status.")
                .toString());
    }

    @Test
    public void testInsertSeat_InvalidType_ShouldFail() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Seat("C", 3, "Available", "SuperVIP");
        }, new StringBuilder()
                .append("Should throw IllegalArgumentException for invalid seat type.")
                .toString());
    }

    @Test
    public void testInsertSeat_WithoutRoom_ShouldFail() {
        Seat seat = new Seat("C", 4, "Available", "Normal");

        boolean result = seatDao.insertSeat(seat);

        assertFalse(result, new StringBuilder()
                .append("Should fail when inserting a seat without associated CinemaRoom.")
                .toString());
    }

    @Test
    public void testGetSeatById() {
        Seat seat = new Seat("E", 10, "Available", "VIP");
        seat.setCinemaRoom(testRoom);
        seatDao.insertSeat(seat);
        Integer id = seat.getSeatId();

        Seat fetched = seatDao.getSeatByID(id);
        assertNotNull(fetched);
        assertEquals("E", fetched.getSeatColumn());
        assertEquals(10, fetched.getSeatRow());
        assertEquals("Available", fetched.getSeatStatus());
        assertEquals("VIP", fetched.getSeatType());
    }

    @Test
    public void testGetAllSeatsByRoom() {
        Seat seat1 = new Seat("A", 1, "Available", "Normal");
        Seat seat2 = new Seat("A", 2, "Available", "Normal");
        Seat seat3 = new Seat("A", 3, "Booked", "VIP");
        seat1.setCinemaRoom(testRoom);
        seat2.setCinemaRoom(testRoom);
        seat3.setCinemaRoom(testRoom);

        seatDao.insertSeat(seat1);
        seatDao.insertSeat(seat2);
        seatDao.insertSeat(seat3);

        List<Seat> allSeats = seatDao.getAllSeat();
        List<Seat> roomSeats = allSeats.stream()
                .filter(s -> s.getCinemaRoom().getCinemaRoomId().equals(testRoom.getCinemaRoomId()))
                .collect(Collectors.toList());

        assertEquals(3, roomSeats.size(), new StringBuilder()
                .append("Expected exactly 3 seats for Room A, but found: ")
                .append(roomSeats.size())
                .toString());
    }

    @Test
    public void testUpdateSeatStatus_Success() {
        Seat seat = new Seat("B", 5, "Available", "Normal");
        seat.setCinemaRoom(testRoom);
        seatDao.insertSeat(seat);
        Integer id = seat.getSeatId();

        Seat updateData = new Seat("B", 5, "Booked", "Normal");
        updateData.setCinemaRoom(testRoom);
        boolean result = seatDao.updateSeatByID(id, updateData);
        assertTrue(result);

        Seat fetched = seatDao.getSeatByID(id);
        assertEquals("Booked", fetched.getSeatStatus(), new StringBuilder()
                .append("Expected seat status updated to Booked, but got ")
                .append(fetched.getSeatStatus())
                .toString());
    }

    @Test
    public void testDeleteSeat_Success() {
        Seat seat = new Seat("D", 8, "Available", "Normal");
        seat.setCinemaRoom(testRoom);
        seatDao.insertSeat(seat);
        Integer id = seat.getSeatId();

        boolean result = seatDao.deleteSeatById(id);
        assertTrue(result);

        Seat fetchedSeat = seatDao.getSeatByID(id);
        assertNull(fetchedSeat, new StringBuilder()
                .append("Deleted seat should not exist in the database.")
                .toString());

        CinemaRoom parentRoom = roomDao.getRoomByID(testRoom.getCinemaRoomId());
        assertNotNull(parentRoom, new StringBuilder()
                .append("Parent CinemaRoom should remain in the database when seat is deleted.")
                .toString());
    }

    @Test
    public void testInsertSeat_Null_ShouldFail() {
        boolean result = seatDao.insertSeat(null);
        assertFalse(result, new StringBuilder()
                .append("Inserting null seat must return false, not crash.")
                .toString());
    }

    @Test
    public void testGetSeatById_InvalidId_ShouldReturnNull() {
        Seat fetched = seatDao.getSeatByID(-4);
        assertNull(fetched, new StringBuilder()
                .append("Invalid negative ID must return null, not crash.")
                .toString());
    }

    @Test
    public void testUpdateSeat_Null_ShouldFail() {
        boolean result = seatDao.updateSeatByID(1, null);
        assertFalse(result, new StringBuilder()
                .append("Updating with null seat data must return false, not crash.")
                .toString());
    }

    @Test
    public void testUpdateSeat_InvalidId_ShouldFail() {
        Seat seat = new Seat("B", 5, "Available", "Normal");
        boolean result = seatDao.updateSeatByID(-1, seat);
        assertFalse(result, new StringBuilder()
                .append("Updating with negative ID must return false, not crash.")
                .toString());
    }

    @Test
    public void testDeleteSeatById_InvalidId_ShouldFail() {
        boolean result = seatDao.deleteSeatById(-10);
        assertFalse(result, new StringBuilder()
                .append("Deleting with negative ID must return false, not crash.")
                .toString());
    }
}
