package fa.training.dao;

import fa.training.entities.CinemaRoom;
import fa.training.entities.Seat;
import fa.training.utils.HibernateUtil;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Integration tests for SeatDao.
 * Room is created as fixture; seats are added individually per test.
 */
public class SeatDaoTest {

    private static final RoomDao roomDao = new RoomDao();
    private static final SeatDao seatDao = new SeatDao();

    private CinemaRoom testRoom;
    private Seat testSeat;

    @Before
    public void setUp() {
        testRoom = roomDao.saveRoom(new CinemaRoom("Seat Test Room", 4));
        testSeat = new Seat("A", 1, "Available", "Normal");
        testSeat.setCinemaRoom(testRoom);
        seatDao.saveSeat(testSeat);
    }

    @After
    public void tearDown() {
        // Cascade delete from room removes all its seats
        roomDao.deleteRoomById(testRoom.getCinemaRoomId());
    }

    @AfterClass
    public static void tearDownClass() {
        HibernateUtil.shutdown();
    }

    @Test
    public void findSeatById_existingId_returnsSeat() {
        Optional<Seat> result = seatDao.findSeatById(testSeat.getSeatId());

        assertTrue("Seat should be present", result.isPresent());
        assertEquals("A", result.get().getSeatColumn());
        assertEquals(1, result.get().getSeatRow());
        assertEquals("Available", result.get().getSeatStatus());
        assertEquals("Normal", result.get().getSeatType());
    }

    @Test
    public void findSeatById_nonExistingId_returnsEmpty() {
        Optional<Seat> result = seatDao.findSeatById(Integer.MAX_VALUE);

        assertFalse("Should be empty for unknown id", result.isPresent());
    }

    @Test
    public void findAllSeats_containsAtLeastTestSeat() {
        List<Seat> seats = seatDao.findAllSeats();

        assertNotNull(seats);
        assertTrue("At least 1 seat expected", seats.size() >= 1);
        boolean found = seats.stream()
                .anyMatch(s -> s.getSeatId() == testSeat.getSeatId());
        assertTrue("Test seat must be in list", found);
    }

    @Test
    public void findSeatsByRoomId_returnsOnlySeatsForThatRoom() {
        List<Seat> seats = seatDao.findSeatsByRoomId(testRoom.getCinemaRoomId());

        assertFalse("Should find at least 1 seat for this room", seats.isEmpty());
        seats.forEach(s ->
            assertEquals("All seats must belong to testRoom",
                    testRoom.getCinemaRoomId(), s.getCinemaRoom().getCinemaRoomId()));
    }

    @Test
    public void findSeatsByStatus_available_includesTestSeat() {
        List<Seat> available = seatDao.findSeatsByStatus("Available");

        boolean found = available.stream()
                .anyMatch(s -> s.getSeatId() == testSeat.getSeatId());
        assertTrue("Test seat (Available) must appear in results", found);
    }

    @Test
    public void saveSeat_assignsGeneratedId() {
        Seat seat = new Seat("B", 2, "Available", "VIP");
        seat.setCinemaRoom(testRoom);
        Seat saved = seatDao.saveSeat(seat);

        assertTrue("Generated id must be > 0", saved.getSeatId() > 0);
        assertEquals("VIP", saved.getSeatType());

        seatDao.deleteSeatById(saved.getSeatId()); // cleanup
    }

    @Test
    public void updateSeatById_existingId_updatesStatusAndType() {
        boolean updated = seatDao.updateSeatById(testSeat.getSeatId(), "Booked", "VIP");

        assertTrue("updateSeatById should return true", updated);

        Optional<Seat> reloaded = seatDao.findSeatById(testSeat.getSeatId());
        assertTrue(reloaded.isPresent());
        assertEquals("Booked", reloaded.get().getSeatStatus());
        assertEquals("VIP", reloaded.get().getSeatType());
    }

    @Test
    public void updateSeatById_nonExistingId_returnsFalse() {
        assertFalse(seatDao.updateSeatById(Integer.MAX_VALUE, "Booked", "VIP"));
    }

    @Test
    public void deleteSeatById_existingId_removesSeat() {
        Seat seat = new Seat("C", 3, "Available", "Normal");
        seat.setCinemaRoom(testRoom);
        Seat saved = seatDao.saveSeat(seat);
        int id = saved.getSeatId();

        boolean deleted = seatDao.deleteSeatById(id);

        assertTrue("deleteSeatById should return true", deleted);
        assertFalse("Seat should no longer exist",
                seatDao.findSeatById(id).isPresent());
    }

    @Test
    public void deleteSeatById_nonExistingId_returnsFalse() {
        assertFalse(seatDao.deleteSeatById(Integer.MAX_VALUE));
    }
}
