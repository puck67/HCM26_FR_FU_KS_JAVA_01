package fa.training.dao;

import fa.training.dao.impl.RoomDaoImpl;
import fa.training.dao.impl.SeatDaoImpl;
import fa.training.entities.CinemaRoom;
import fa.training.entities.Seat;
import fa.training.utils.HibernateUtil;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SeatDaoTest {

    private RoomDao roomDao;
    private SeatDao seatDao;

    @BeforeEach
    public void setUp() {
         roomDao = new RoomDaoImpl();
         seatDao = new SeatDaoImpl();
    }

    @Test
    public void testSeatCRUD() {
        // Create Room first
        CinemaRoom room = new CinemaRoom("Room 102", 50);
        roomDao.insertRoom(room);

        // Create Seat
        Seat seat = new Seat("C", 5, "Available", "VIP");
        seat.setCinemaRoom(room);
        assertTrue(seatDao.insertSeat(seat), "Should insert Seat successfully");
        assertTrue(seat.getSeatId() > 0, "Seat ID should be generated");

        // Read
        Seat fetched = seatDao.getSeatById(seat.getSeatId());
        assertNotNull(fetched);
        assertEquals("C", fetched.getSeatColumn());
        assertEquals(5, fetched.getSeatRow());
        assertEquals("Available", fetched.getSeatStatus());
        assertEquals("VIP", fetched.getSeatType());

        // Update
        fetched.setSeatStatus("Booked");
        assertTrue(seatDao.updateSeatById(fetched), "Should update Seat successfully");

        Seat updated = seatDao.getSeatById(seat.getSeatId());
        assertEquals("Booked", updated.getSeatStatus());

        // List
        List<Seat> seats = seatDao.getAllSeat();
        assertTrue(seats.size() > 0, "Seats list should not be empty");

        // Delete
        assertTrue(seatDao.deleteSeatById(seat.getSeatId()), "Should delete Seat successfully");
        assertNull(seatDao.getSeatById(seat.getSeatId()));
    }
}
