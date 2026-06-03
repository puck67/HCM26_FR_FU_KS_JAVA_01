package fa.training;

import fa.training.dao.RoomDao;
import fa.training.dao.SeatDao;
import fa.training.entities.CinemaRoom;
import fa.training.entities.Seat;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SeatDaoTest {

    private static final SeatDao dao = new SeatDao();
    private static final RoomDao roomDao = new RoomDao();
    private static CinemaRoom sharedRoom;

    @BeforeAll
    static void createSharedRoom() {
        sharedRoom = new CinemaRoom("SeatTestRoom_" + System.nanoTime(), 120);
        roomDao.save(sharedRoom);
    }

    @AfterAll
    static void deleteSharedRoom() {
        roomDao.deleteById(sharedRoom.getCinemaRoomId());
    }

    @Test
    void saveSeat() {
        Seat seat = new Seat(sharedRoom, "A", 1, "Available", "VIP");

        dao.save(seat);

        assertTrue(seat.getSeatId() > 0);

        dao.deleteById(seat.getSeatId());
    }

    @Test
    void findSeatById() {
        Seat seat = new Seat(sharedRoom, "B", 2, "Available", "Normal");
        dao.save(seat);

        Optional<Seat> found = dao.findById(seat.getSeatId());

        assertTrue(found.isPresent());
        assertEquals("B", found.get().getSeatColumn());
        assertEquals(2, found.get().getSeatRow());
        assertEquals("Normal", found.get().getSeatType());

        dao.deleteById(seat.getSeatId());
    }

    @Test
    void findAllSeats() {
        Seat s1 = new Seat(sharedRoom, "C", 3, "Available", "Normal");
        Seat s2 = new Seat(sharedRoom, "D", 4, "Available", "VIP");
        dao.save(s1);
        dao.save(s2);

        List<Seat> all = dao.findAll();

        assertTrue(all.size() >= 2);

        dao.deleteById(s1.getSeatId());
        dao.deleteById(s2.getSeatId());
    }

    @Test
    void updateSeat() {
        Seat seat = new Seat(sharedRoom, "E", 5, "Available", "Normal");
        dao.save(seat);

        seat.setSeatStatus("Booked");
        seat.setSeatType("VIP");
        dao.update(seat);

        Optional<Seat> found = dao.findById(seat.getSeatId());
        assertTrue(found.isPresent());
        assertEquals("Booked", found.get().getSeatStatus());
        assertEquals("VIP", found.get().getSeatType());

        dao.deleteById(seat.getSeatId());
    }

    @Test
    void deleteSeatById() {
        Seat seat = new Seat(sharedRoom, "F", 6, "Not Available", "Normal");
        dao.save(seat);
        int id = seat.getSeatId();

        dao.deleteById(id);

        Optional<Seat> found = dao.findById(id);
        assertFalse(found.isPresent());
    }
}
