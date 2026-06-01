package fa.training.dao;

import fa.training.dao.impl.RoomDaoImpl;
import fa.training.dao.impl.SeatDaoImpl;
import fa.training.entities.CinemaRoom;
import fa.training.entities.Seat;
import fa.training.util.HibernateUtil;
import org.hibernate.Session;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SeatDaoTest {

    private RoomDao roomDao;
    private SeatDao seatDao;

    @BeforeAll
    public static void setUpClass() {
        System.setProperty("hibernate.config.file", "hibernate-test.cfg.xml");
        HibernateUtil.getSessionFactory();
    }

    @BeforeEach
    public void setUp() {
        roomDao = new RoomDaoImpl();
        seatDao = new SeatDaoImpl();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.createMutationQuery("DELETE FROM CinemaRoomDetail").executeUpdate();
            session.createMutationQuery("DELETE FROM Seat").executeUpdate();
            session.createMutationQuery("DELETE FROM CinemaRoom").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test
    public void testInsertAndGetSeatByID() {
        CinemaRoom room = new CinemaRoom("Room 1", 50);
        roomDao.insertRoom(room);

        Seat seat = new Seat("C", 5, "Available", "Normal");
        seat.setCinemaRoom(room);
        seatDao.insertSeat(seat);

        assertTrue(seat.getSeatId() > 0);

        Seat fetched = seatDao.getSeatByID(seat.getSeatId());
        assertNotNull(fetched);
        assertEquals("C", fetched.getSeatColumn());
        assertEquals(5, fetched.getSeatRow());
        assertEquals("Available", fetched.getSeatStatus());
        assertEquals("Normal", fetched.getSeatType());
        assertEquals("Room 1", fetched.getCinemaRoom().getCinemaRoomName());
    }

    @Test
    public void testUpdateSeatByID() {
        CinemaRoom room = new CinemaRoom("Room 1", 50);
        roomDao.insertRoom(room);

        Seat seat = new Seat("D", 8, "Available", "VIP");
        seat.setCinemaRoom(room);
        seatDao.insertSeat(seat);

        seat.setSeatStatus("Booked");
        seat.setSeatType("Normal");
        seatDao.updateSeatByID(seat);

        Seat fetched = seatDao.getSeatByID(seat.getSeatId());
        assertNotNull(fetched);
        assertEquals("Booked", fetched.getSeatStatus());
        assertEquals("Normal", fetched.getSeatType());
    }

    @Test
    public void testDeleteSeatById() {
        CinemaRoom room = new CinemaRoom("Room 1", 50);
        roomDao.insertRoom(room);

        Seat seat = new Seat("E", 10, "Available", "Normal");
        seat.setCinemaRoom(room);
        seatDao.insertSeat(seat);

        int seatId = seat.getSeatId();
        seatDao.deleteSeatById(seatId);

        assertNull(seatDao.getSeatByID(seatId));
    }

    @Test
    public void testGetAllSeats() {
        CinemaRoom room = new CinemaRoom("Room A", 30);
        roomDao.insertRoom(room);

        Seat s1 = new Seat("A", 1, "Available", "Normal");
        s1.setCinemaRoom(room);
        Seat s2 = new Seat("A", 2, "Available", "Normal");
        s2.setCinemaRoom(room);

        seatDao.insertSeat(s1);
        seatDao.insertSeat(s2);

        List<Seat> list = seatDao.getAllSeats();
        assertEquals(2, list.size());
    }
}
