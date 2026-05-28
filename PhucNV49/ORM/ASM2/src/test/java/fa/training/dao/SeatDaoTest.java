package fa.training.dao;

import fa.training.dao.impl.RoomDaoImpl;
import fa.training.dao.impl.SeatDaoImpl;
import fa.training.entities.CinemaRoom;
import fa.training.entities.Seat;
import fa.training.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SeatDaoTest {
    private final RoomDao roomDao = new RoomDaoImpl();
    private final SeatDao seatDao = new SeatDaoImpl();
    private CinemaRoom defaultRoom;

    @BeforeEach
    public void cleanUp() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.createMutationQuery("delete from Seat").executeUpdate();
            session.createMutationQuery("delete from CinemaRoomDetail").executeUpdate();
            session.createMutationQuery("delete from CinemaRoom").executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.err.println("CleanUp failed: " + e.getMessage());
        }

        // Create a default room for seat operations
        defaultRoom = new CinemaRoom("Test Room", 50);
        roomDao.insertRoom(defaultRoom);
    }

    @Test
    public void testInsertSeat() {
        Seat seat = new Seat("A", 1, "Available", "Normal");
        defaultRoom.addSeat(seat);
        
        boolean success = seatDao.insertSeat(seat);
        assertTrue(success);
        assertNotEquals(0, seat.getSeatId());
    }

    @Test
    public void testGetSeatByID() {
        Seat seat = new Seat("B", 2, "Booked", "VIP");
        defaultRoom.addSeat(seat);
        seatDao.insertSeat(seat);

        Seat fetched = seatDao.getSeatByID(seat.getSeatId());
        assertNotNull(fetched);
        assertEquals("B", fetched.getSeatColumn());
        assertEquals(2, fetched.getSeatRow());
        assertEquals("Booked", fetched.getSeatStatus());
        assertEquals("VIP", fetched.getSeatType());
    }

    @Test
    public void testGetAllSeat() {
        Seat s1 = new Seat("C", 3, "Available", "Normal");
        Seat s2 = new Seat("D", 4, "Available", "Normal");
        defaultRoom.addSeat(s1);
        defaultRoom.addSeat(s2);

        seatDao.insertSeat(s1);
        seatDao.insertSeat(s2);

        List<Seat> seats = seatDao.getAllSeat();
        assertEquals(2, seats.size());
    }

    @Test
    public void testUpdateSeatByID() {
        Seat seat = new Seat("E", 5, "Available", "Normal");
        defaultRoom.addSeat(seat);
        seatDao.insertSeat(seat);

        seat.setSeatStatus("Booked");
        seat.setSeatType("VIP");
        boolean success = seatDao.updateSeatByID(seat);
        assertTrue(success);

        Seat updated = seatDao.getSeatByID(seat.getSeatId());
        assertEquals("Booked", updated.getSeatStatus());
        assertEquals("VIP", updated.getSeatType());
    }

    @Test
    public void testDeleteSeatById() {
        Seat seat = new Seat("F", 6, "Available", "Normal");
        defaultRoom.addSeat(seat);
        seatDao.insertSeat(seat);

        boolean success = seatDao.deleteSeatById(seat.getSeatId());
        assertTrue(success);

        Seat fetched = seatDao.getSeatByID(seat.getSeatId());
        assertNull(fetched);
    }
}
