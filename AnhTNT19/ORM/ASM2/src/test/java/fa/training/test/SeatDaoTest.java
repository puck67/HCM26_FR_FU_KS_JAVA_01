package fa.training.test;

import fa.training.dao.RoomDao;
import fa.training.dao.SeatDao;
import fa.training.entities.CinemaRoom;
import fa.training.entities.Seat;
import fa.training.utils.HibernateUtils;
import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SeatDaoTest {

    private static SeatDao seatDao;
    private static RoomDao roomDao;
    private static int savedSeatId;
    private static int helperRoomId;

    @BeforeAll
    static void setUp() {
        seatDao = new SeatDao();
        roomDao = new RoomDao();
        CinemaRoom room = new CinemaRoom("Seat Test Room", 20);
        roomDao.insertRoom(room);
        helperRoomId = room.getCinemaRoomId();
    }

    @AfterAll
    static void tearDown() {
        roomDao.deleteRoomById(helperRoomId);
        HibernateUtils.shutdown();
    }

    @Test @Order(1)
    void testInsertSeat() {
        Seat seat = new Seat("A", 1, "Available", "VIP");
        seat.setCinemaRoom(roomDao.getRoomById(helperRoomId));
        assertTrue(seatDao.insertSeat(seat));
        savedSeatId = seat.getSeatId();
    }

    @Test @Order(2)
    void testGetSeatById() {
        Seat seat = seatDao.getSeatById(savedSeatId);
        assertNotNull(seat);
        assertEquals("Available", seat.getSeatStatus());
    }

    @Test @Order(3)
    void testGetAllSeats() {
        assertFalse(seatDao.getAllSeats().isEmpty());
    }

    @Test @Order(4)
    void testUpdateSeatById() {
        assertTrue(seatDao.updateSeatById(savedSeatId, "Not Available", "Normal"));
        assertEquals("Not Available", seatDao.getSeatById(savedSeatId).getSeatStatus());
    }

    @Test @Order(5)
    void testGetSeatsByRoomId() {
        List<Seat> seats = seatDao.getSeatsByRoomId(helperRoomId);
        assertFalse(seats.isEmpty());
    }

    @Test @Order(6)
    void testGetSeatsByStatus() {
        List<Seat> seats = seatDao.getSeatsByStatus("Not Available");
        assertTrue(seats.stream().anyMatch(s -> s.getSeatId() == savedSeatId));
    }

    @Test @Order(7)
    void testBookSeat() {
        seatDao.updateSeatById(savedSeatId, "Available", "VIP");
        assertTrue(seatDao.bookSeat(savedSeatId));
        assertEquals("Booked", seatDao.getSeatById(savedSeatId).getSeatStatus());
    }

    @Test @Order(8)
    void testBookSeatAlreadyBooked() {
        assertFalse(seatDao.bookSeat(savedSeatId));
    }

    @Test @Order(9)
    void testDeleteSeatById() {
        assertTrue(seatDao.deleteSeatById(savedSeatId));
        assertNull(seatDao.getSeatById(savedSeatId));
    }
}