package fa.training.dao;

import fa.training.dao.impl.RoomDaoImpl;
import fa.training.dao.impl.SeatDaoImpl;
import fa.training.entities.CinemaRoom;
import fa.training.entities.Seat;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

import static org.junit.Assert.*;

public class SeatDaoTest extends BaseDaoTest {

    private RoomDao roomDao;
    private SeatDao seatDao;

    @Before
    @Override
    public void setUp() {
        super.setUp();
        roomDao = new RoomDaoImpl();
        seatDao = new SeatDaoImpl();
    }

    @Test
    public void testInsertSeat() {
        CinemaRoom room = new CinemaRoom("Seat Test Room", 50);
        roomDao.insertRoom(room);

        Seat seat = new Seat("F", 12, "Available", "VIP");
        seat.setCinemaRoom(room);

        boolean result = seatDao.insertSeat(seat);
        assertTrue("Insert seat should return true", result);
        assertTrue("Seat ID should be generated", seat.getSeatId() > 0);

        Seat retrieved = seatDao.getSeatById(seat.getSeatId());
        assertNotNull("Retrieved seat should not be null", retrieved);
        assertEquals("F", retrieved.getSeatColumn());
        assertEquals(12, retrieved.getSeatRow());
        assertEquals("Available", retrieved.getSeatStatus());
        assertEquals("VIP", retrieved.getSeatType());
        assertEquals(room.getCinemaRoomId(), retrieved.getCinemaRoom().getCinemaRoomId());
    }

    @Test
    public void testGetSeatById() {
        CinemaRoom room = new CinemaRoom("Room 5", 60);
        roomDao.insertRoom(room);

        Seat seat = new Seat("B", 5, "Booked", "Normal");
        seat.setCinemaRoom(room);
        seatDao.insertSeat(seat);

        Seat retrieved = seatDao.getSeatById(seat.getSeatId());
        assertNotNull(retrieved);
        assertEquals("Booked", retrieved.getSeatStatus());

        Seat nonExisting = seatDao.getSeatById(-999);
        assertNull(nonExisting);
    }

    @Test
    public void testGetAllSeats() {
        CinemaRoom room = new CinemaRoom("Room 6", 80);
        roomDao.insertRoom(room);

        Seat seat1 = new Seat("C", 1, "Available", "Normal");
        seat1.setCinemaRoom(room);
        Seat seat2 = new Seat("C", 2, "Available", "Normal");
        seat2.setCinemaRoom(room);

        seatDao.insertSeat(seat1);
        seatDao.insertSeat(seat2);

        List<Seat> seats = seatDao.getAllSeats();
        assertNotNull(seats);
        assertEquals(2, seats.size());
    }

    @Test
    public void testUpdateSeatById() {
        CinemaRoom room = new CinemaRoom("Room 7", 40);
        roomDao.insertRoom(room);

        Seat seat = new Seat("D", 8, "Available", "VIP");
        seat.setCinemaRoom(room);
        seatDao.insertSeat(seat);

        seat.setSeatStatus("Booked");
        seat.setSeatType("Normal");
        boolean result = seatDao.updateSeatById(seat);
        assertTrue(result);

        Seat updated = seatDao.getSeatById(seat.getSeatId());
        assertEquals("Booked", updated.getSeatStatus());
        assertEquals("Normal", updated.getSeatType());
    }

    @Test
    public void testDeleteSeatById() {
        CinemaRoom room = new CinemaRoom("Room 8", 40);
        roomDao.insertRoom(room);

        Seat seat = new Seat("E", 10, "Available", "Normal");
        seat.setCinemaRoom(room);
        seatDao.insertSeat(seat);

        boolean result = seatDao.deleteSeatById(seat.getSeatId());
        assertTrue(result);

        Seat deleted = seatDao.getSeatById(seat.getSeatId());
        assertNull(deleted);

        // Delete non-existing seat should return false
        boolean resultNonExisting = seatDao.deleteSeatById(-999);
        assertFalse(resultNonExisting);
    }
}
