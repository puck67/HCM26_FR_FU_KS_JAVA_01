package fa.training.dao;

import fa.training.entities.CinemaRoom;
import fa.training.entities.Seat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class SeatDaoTest {

    private RoomDao roomDao;
    private SeatDao seatDao;
    private CinemaRoom testRoom;
    private Seat testSeat;

    @BeforeEach
    public void setUp() {
        roomDao = new RoomDao();
        seatDao = new SeatDao();

        testRoom = new CinemaRoom("Seat Room", 50);
        roomDao.insert(testRoom);

        testSeat = new Seat(testRoom, "A", 1, "Available", "Normal");
        seatDao.insert(testSeat);
    }

    @AfterEach
    public void tearDown() {
        if (testSeat != null && testSeat.getSeatId() != null) {
            seatDao.deleteById(testSeat.getSeatId());
        }
        if (testRoom != null && testRoom.getCinemaRoomId() != null) {
            roomDao.deleteById(testRoom.getCinemaRoomId());
        }
    }

    @Test
    public void testInsert() {
        Seat seat = new Seat(testRoom, "B", 1, "Available", "VIP");
        boolean result = seatDao.insert(seat);
        assertTrue(result, "Insert should return true");
        assertNotNull(seat.getSeatId(), "Seat ID should be generated");

        seatDao.deleteById(seat.getSeatId());
    }

    @Test
    public void testGetById() {
        Seat retrieved = seatDao.getById(testSeat.getSeatId());
        assertNotNull(retrieved, "Should retrieve seat");
        assertEquals("A", retrieved.getSeatColumn(), "Column should match");
        assertEquals("Normal", retrieved.getSeatType(), "Type should match");
    }

    @Test
    public void testUpdateById() {
        testSeat.setSeatStatus("Booked");
        boolean updateResult = seatDao.updateById(testSeat);
        assertTrue(updateResult, "Update should return true");

        Seat retrieved = seatDao.getById(testSeat.getSeatId());
        assertEquals("Booked", retrieved.getSeatStatus(), "Status should be updated");
    }

    @Test
    public void testDeleteById() {
        Seat seat = new Seat(testRoom, "C", 2, "Available", "Normal");
        seatDao.insert(seat);
        
        Integer id = seat.getSeatId();
        boolean deleteResult = seatDao.deleteById(id);
        assertTrue(deleteResult, "Delete should return true");

        assertNull(seatDao.getById(id), "Should not find deleted entity");
    }

    @Test
    public void testGetAll() {
        List<Seat> seats = seatDao.getAll();
        assertNotNull(seats, "List should not be null");
        assertFalse(seats.isEmpty(), "List should not be empty");
    }
}
