package fa.training.dao;

import fa.training.entities.CinemaRoom;
import fa.training.entities.Seat;
import fa.training.utils.HibernateUtil;
import org.junit.*;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for SeatDAO.
 * Tests all 5 CRUD methods + getSeatsByRoomID.
 */
public class SeatDAOTest {

    private static SeatDAO seatDAO;
    private static RoomDAO roomDAO;

    // Shared room used across tests
    private static CinemaRoom sharedRoom;

    @BeforeClass
    public static void setUp() {
        seatDAO = new SeatDAO();
        roomDAO = new RoomDAO();

        // Create a shared room for seat tests
        sharedRoom = new CinemaRoom("Seat Test Room", 10);
        roomDAO.insertRoom(sharedRoom);
    }

    @AfterClass
    public static void tearDown() {
        // Cascade deletes seats too
        roomDAO.deleteRoomByID(sharedRoom.getCinemaRoomId());
        HibernateUtil.shutdown();
    }

    // ---- Test insertSeat & getSeatByID ----

    @Test
    public void testInsertAndGetSeatByID() {
        Seat seat = new Seat("C", 3, "Available", "Normal");
        seat.setCinemaRoom(sharedRoom);
        seatDAO.insertSeat(seat);

        int id = seat.getSeatId();
        assertTrue("ID should be > 0 after insert", id > 0);

        Seat fetched = seatDAO.getSeatByID(id);
        assertNotNull("Fetched seat should not be null", fetched);
        assertEquals("C", fetched.getSeatColumn());
        assertEquals(3, fetched.getSeatRow());
        assertEquals("Available", fetched.getSeatStatus());
        assertEquals("Normal", fetched.getSeatType());

        // cleanup
        seatDAO.deleteSeatByID(id);
    }

    // ---- Test getAllSeats ----

    @Test
    public void testGetAllSeats() {
        Seat s1 = new Seat("D", 1, "Available", "VIP");
        Seat s2 = new Seat("D", 2, "Available", "Normal");
        s1.setCinemaRoom(sharedRoom);
        s2.setCinemaRoom(sharedRoom);
        seatDAO.insertSeat(s1);
        seatDAO.insertSeat(s2);

        List<Seat> all = seatDAO.getAllSeats();
        assertNotNull("List should not be null", all);
        assertTrue("Should have at least 2 seats", all.size() >= 2);

        // cleanup
        seatDAO.deleteSeatByID(s1.getSeatId());
        seatDAO.deleteSeatByID(s2.getSeatId());
    }

    // ---- Test getSeatsByRoomID ----

    @Test
    public void testGetSeatsByRoomID() {
        Seat s = new Seat("E", 1, "Available", "Normal");
        s.setCinemaRoom(sharedRoom);
        seatDAO.insertSeat(s);

        List<Seat> roomSeats = seatDAO.getSeatsByRoomID(sharedRoom.getCinemaRoomId());
        assertNotNull(roomSeats);
        assertTrue("Should find at least 1 seat in room", roomSeats.size() >= 1);

        // cleanup
        seatDAO.deleteSeatByID(s.getSeatId());
    }

    // ---- Test updateSeatByID ----

    @Test
    public void testUpdateSeatByID() {
        Seat seat = new Seat("F", 1, "Available", "Normal");
        seat.setCinemaRoom(sharedRoom);
        seatDAO.insertSeat(seat);
        int id = seat.getSeatId();

        seatDAO.updateSeatByID(id, "Booked", "VIP");

        Seat updated = seatDAO.getSeatByID(id);
        assertNotNull(updated);
        assertEquals("Booked", updated.getSeatStatus());
        assertEquals("VIP", updated.getSeatType());

        // cleanup
        seatDAO.deleteSeatByID(id);
    }

    // ---- Test deleteSeatByID ----

    @Test
    public void testDeleteSeatByID() {
        Seat seat = new Seat("G", 1, "Available", "Normal");
        seat.setCinemaRoom(sharedRoom);
        seatDAO.insertSeat(seat);
        int id = seat.getSeatId();

        seatDAO.deleteSeatByID(id);

        Seat deleted = seatDAO.getSeatByID(id);
        assertNull("Seat should be null after deletion", deleted);
    }
}
