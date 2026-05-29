package fa.training;

import fa.training.dao.impl.RoomDaoImpl;
import fa.training.dao.impl.SeatDaoImpl;
import fa.training.entities.CinemaRoom;
import fa.training.entities.Seat;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SeatDaoTest {

    private static final SeatDaoImpl seatDao = new SeatDaoImpl();
    private static final RoomDaoImpl roomDao = new RoomDaoImpl();
    private static int savedRoomId;
    private static int savedSeatId;

    @BeforeAll
    static void setup() {
        CinemaRoom room = new CinemaRoom("Seat Test Room", 30);
        roomDao.insert(room);
        savedRoomId = room.getCinemaRoomId();
    }

    @AfterAll
    static void cleanup() {
        roomDao.deleteById(savedRoomId);
    }

    @Test
    @Order(1)
    @DisplayName("Insert a new Seat")
    void testInsert() {
        CinemaRoom room = roomDao.getById(savedRoomId).orElseThrow();
        Seat seat = new Seat(room, "A", 1, "Available", "Normal");
        seatDao.insert(seat);
        assertTrue(seat.getSeatId() > 0);
        savedSeatId = seat.getSeatId();
    }

    @Test
    @Order(2)
    @DisplayName("Get Seat by ID")
    void testGetById() {
        Optional<Seat> result = seatDao.getById(savedSeatId);
        assertTrue(result.isPresent());
        assertEquals("A", result.get().getSeatColumn());
        assertEquals(1, result.get().getSeatRow());
        assertEquals("Available", result.get().getSeatStatus());
        assertEquals("Normal", result.get().getSeatType());
    }

    @Test
    @Order(3)
    @DisplayName("Get all Seats")
    void testGetAll() {
        List<Seat> seats = seatDao.getAll();
        assertFalse(seats.isEmpty());
    }

    @Test
    @Order(4)
    @DisplayName("Get Seats by Room ID")
    void testGetByRoomId() {
        List<Seat> seats = seatDao.getByRoomId(savedRoomId);
        assertFalse(seats.isEmpty());
        seats.forEach(s -> assertEquals(savedRoomId, s.getCinemaRoom().getCinemaRoomId()));
    }

    @Test
    @Order(5)
    @DisplayName("Get Seats by Status")
    void testGetByStatus() {
        List<Seat> seats = seatDao.getByStatus("Available");
        assertFalse(seats.isEmpty());
        seats.forEach(s -> assertEquals("Available", s.getSeatStatus()));
    }

    @Test
    @Order(6)
    @DisplayName("Get Seats by Type")
    void testGetByType() {
        List<Seat> seats = seatDao.getByType("Normal");
        assertFalse(seats.isEmpty());
        seats.forEach(s -> assertEquals("Normal", s.getSeatType()));
    }

    @Test
    @Order(7)
    @DisplayName("Update Seat by ID")
    void testUpdateById() {
        Seat seat = seatDao.getById(savedSeatId).orElseThrow();
        seat.setSeatStatus("Booked");
        seat.setSeatType("VIP");
        seat.setSeatColumn("B");
        seat.setSeatRow(2);
        seatDao.updateById(seat);

        Seat updated = seatDao.getById(savedSeatId).orElseThrow();
        assertEquals("Booked", updated.getSeatStatus());
        assertEquals("VIP", updated.getSeatType());
        assertEquals("B", updated.getSeatColumn());
        assertEquals(2, updated.getSeatRow());
    }

    @Test
    @Order(8)
    @DisplayName("Delete Seat by ID")
    void testDeleteById() {
        seatDao.deleteById(savedSeatId);
        Optional<Seat> result = seatDao.getById(savedSeatId);
        assertFalse(result.isPresent());
    }
}
