package fa.training.dao;

import fa.training.dao.impl.RoomDaoImpl;
import fa.training.dao.impl.SeatDaoImpl;
import fa.training.entities.CinemaRoom;
import fa.training.entities.Seat;
import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SeatDaoTest {

    private static RoomDao roomDao;
    private static SeatDao seatDao;
    private static CinemaRoom testRoom;
    private static int testSeatId;

    @BeforeAll
    public static void setup() {
        roomDao = new RoomDaoImpl();
        seatDao = new SeatDaoImpl();

        // Setup a temporary cinema room to link seats to
        testRoom = new CinemaRoom("Seat Test Room", 100);
        roomDao.insertRoom(testRoom);
    }

    @AfterAll
    public static void tearDown() {
        if (testRoom != null) {
            roomDao.deleteRoomById(testRoom.getCinemaRoomId());
        }
    }

    @Test
    @Order(1)
    public void testInsertSeat() {
        Seat seat = new Seat("F", 7, "Available", "VIP");
        seat.setCinemaRoom(testRoom);

        boolean result = seatDao.insertSeat(seat);
        assertTrue(result, "Seat should be inserted successfully");
        assertTrue(seat.getSeatId() > 0, "Generated ID should be set on seat");
        testSeatId = seat.getSeatId();
    }

    @Test
    @Order(2)
    public void testGetSeatById() {
        Seat seat = seatDao.getSeatById(testSeatId);
        assertNotNull(seat, "Should find the seat by ID");
        assertEquals("F", seat.getSeatColumn());
        assertEquals(7, seat.getSeatRow());
        assertEquals("Available", seat.getSeatStatus());
        assertEquals("VIP", seat.getSeatType());
        assertNotNull(seat.getCinemaRoom(), "Associated Cinema Room should not be null");
    }

    @Test
    @Order(3)
    public void testGetAllSeats() {
        List<Seat> seats = seatDao.getAllSeats();
        assertNotNull(seats, "Seats list should not be null");
        assertTrue(seats.size() > 0, "Seats list should contain at least our test seat");
    }

    @Test
    @Order(4)
    public void testUpdateSeatById() {
        Seat seat = seatDao.getSeatById(testSeatId);
        assertNotNull(seat);
        seat.setSeatStatus("Booked");
        seat.setSeatType("VIP");

        boolean result = seatDao.updateSeatById(seat);
        assertTrue(result, "Seat update should be successful");

        Seat updated = seatDao.getSeatById(testSeatId);
        assertEquals("Booked", updated.getSeatStatus());
    }

    @Test
    @Order(5)
    public void testDeleteSeatById() {
        boolean result = seatDao.deleteSeatById(testSeatId);
        assertTrue(result, "Seat deletion should be successful");

        Seat deleted = seatDao.getSeatById(testSeatId);
        assertNull(deleted, "Seat should no longer exist in the database");
    }
}
