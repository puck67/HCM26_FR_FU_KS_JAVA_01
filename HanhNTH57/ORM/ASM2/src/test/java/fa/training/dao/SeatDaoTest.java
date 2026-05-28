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

    @BeforeAll
    static void setUp() {
        roomDao = new RoomDaoImpl();
        seatDao = new SeatDaoImpl();
    }

    @AfterAll
    static void tearDown() {
        // Shared SessionFactory should not be closed here if other tests are running
    }

    private static int insertedId;

    @Test
    @Order(1)
    void testInsertSeat() {
        CinemaRoom room = new CinemaRoom("Room 3", 120);
        roomDao.insertRoom(room);

        Seat seat = new Seat("A", 1, "Available", "VIP");
        seat.setCinemaRoom(room);
        
        assertTrue(seatDao.insertSeat(seat));
        insertedId = seat.getSeatId();
        assertTrue(insertedId > 0);
    }

    @Test
    @Order(2)
    void testGetSeatById() {
        Seat seat = seatDao.getSeatById(insertedId);
        assertNotNull(seat);
        assertEquals("A", seat.getSeatColumn());
    }

    @Test
    @Order(3)
    void testGetAllSeats() {
        List<Seat> seats = seatDao.getAllSeats();
        assertFalse(seats.isEmpty());
    }

    @Test
    @Order(4)
    void testUpdateSeatById() {
        Seat seat = new Seat("B", 2, "Booked", "Normal");
        assertTrue(seatDao.updateSeatById(insertedId, seat));
        Seat updatedSeat = seatDao.getSeatById(insertedId);
        assertEquals("B", updatedSeat.getSeatColumn());
        assertEquals("Booked", updatedSeat.getSeatStatus());
    }

    @Test
    @Order(5)
    void testDeleteSeatById() {
        assertTrue(seatDao.deleteSeatById(insertedId));
        assertNull(seatDao.getSeatById(insertedId));
    }
}
