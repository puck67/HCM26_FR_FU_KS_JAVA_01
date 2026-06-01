package fa.training.daos;

import fa.training.entities.CinemaRoom;
import fa.training.entities.CinemaRoomDetail;
import fa.training.entities.Seat;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

/**
 * SeatDaoTest - Unit tests for SeatDao
 */
public class SeatDaoTest {
    private static SeatDao seatDao;
    private static RoomDao roomDao;
    private static Integer testRoomId;

    @BeforeClass
    public static void setUp() {
        seatDao = new SeatDao();
        roomDao = new RoomDao();

        // Create test cinema room with details
        CinemaRoom room = new CinemaRoom("Test Cinema Room A", 50);
        CinemaRoomDetail detail = new CinemaRoomDetail(150, LocalDate.now(), "Test Room Detail");
        room.setRoomDetail(detail);
        
        testRoomId = roomDao.insertRoom(room);
        System.out.println("Test setup completed with Room ID: " + testRoomId);
    }

    @Test
    public void testInsertSeat() {
        Seat seat = new Seat(1, "1", "Available", "VIP");
        CinemaRoom room = roomDao.getRoomById(testRoomId);
        seat.setCinemaRoom(room);

        Integer seatId = seatDao.insertSeat(seat);
        assertNotNull(seatId);
        assertTrue(seatId > 0);
        System.out.println("✓ testInsertSeat passed with ID: " + seatId);
    }

    @Test
    public void testGetSeatById() {
        Seat seat = new Seat(2, "2", "Available", "Normal");
        CinemaRoom room = roomDao.getRoomById(testRoomId);
        seat.setCinemaRoom(room);
        Integer seatId = seatDao.insertSeat(seat);

        Seat retrievedSeat = seatDao.getSeatById(seatId);
        assertNotNull(retrievedSeat);
        assertEquals(Integer.valueOf(2), retrievedSeat.getSeatRow());
        assertEquals("2", retrievedSeat.getSeatColumn());
        System.out.println("✓ testGetSeatById passed");
    }

    @Test
    public void testGetAllSeats() {
        CinemaRoom room = roomDao.getRoomById(testRoomId);
        // Insert multiple seats
        for (int i = 1; i <= 3; i++) {
            Seat seat = new Seat(i, "3", "Available", "Normal");
            seat.setCinemaRoom(room);
            seatDao.insertSeat(seat);
        }

        List<Seat> allSeats = seatDao.getAllSeats();
        assertNotNull(allSeats);
        assertTrue(allSeats.size() >= 3);
        System.out.println("✓ testGetAllSeats passed with " + allSeats.size() + " seats");
    }

    @Test
    public void testGetSeatsByRoomId() {
        List<Seat> seatsInRoom = seatDao.getSeatsByRoomId(testRoomId);
        assertNotNull(seatsInRoom);
        System.out.println("✓ testGetSeatsByRoomId passed with " + seatsInRoom.size() + " seats");
    }

    @Test
    public void testGetSeatsByStatus() {
        CinemaRoom room = roomDao.getRoomById(testRoomId);
        Seat seat = new Seat(4, "4", "Booked", "VIP");
        seat.setCinemaRoom(room);
        seatDao.insertSeat(seat);

        List<Seat> bookedSeats = seatDao.getSeatsByStatus("Booked");
        assertNotNull(bookedSeats);
        assertTrue(bookedSeats.size() >= 1);
        System.out.println("✓ testGetSeatsByStatus passed");
    }

    @Test
    public void testUpdateSeatById() {
        Seat seat = new Seat(5, "5", "Available", "Normal");
        CinemaRoom room = roomDao.getRoomById(testRoomId);
        seat.setCinemaRoom(room);
        Integer seatId = seatDao.insertSeat(seat);

        Seat seatToUpdate = new Seat(5, "5", "Booked", "VIP");
        seatDao.updateSeatById(seatId, seatToUpdate);

        Seat updatedSeat = seatDao.getSeatById(seatId);
        assertNotNull(updatedSeat);
        assertEquals("Booked", updatedSeat.getSeatStatus());
        assertEquals("VIP", updatedSeat.getSeatType());
        System.out.println("✓ testUpdateSeatById passed");
    }

    @Test
    public void testDeleteSeatById() {
        Seat seat = new Seat(6, "6", "Available", "Normal");
        CinemaRoom room = roomDao.getRoomById(testRoomId);
        seat.setCinemaRoom(room);
        Integer seatId = seatDao.insertSeat(seat);

        seatDao.deleteSeatById(seatId);
        Seat deletedSeat = seatDao.getSeatById(seatId);
        assertNull(deletedSeat);
        System.out.println("✓ testDeleteSeatById passed");
    }

    @Test
    public void testCountAllSeats() {
        Long count = seatDao.countAllSeats();
        assertNotNull(count);
        assertTrue(count >= 0);
        System.out.println("✓ testCountAllSeats passed with count: " + count);
    }
}
