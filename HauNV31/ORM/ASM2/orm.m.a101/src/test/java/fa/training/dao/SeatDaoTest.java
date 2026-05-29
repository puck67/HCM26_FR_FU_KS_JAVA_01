package fa.training.dao;

import fa.training.entities.CinemaRoom;
import fa.training.entities.Seat;
import fa.training.util.HibernateUtil;
import org.hibernate.SessionFactory;
import org.junit.*;

import java.util.List;

import static org.junit.Assert.*;

public class SeatDaoTest {

    private static SessionFactory sessionFactory;
    private RoomDao roomDao;
    private SeatDao seatDao;

    @BeforeClass
    public static void setUpClass() {
        sessionFactory = new org.hibernate.cfg.Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();
        HibernateUtil.setSessionFactory(sessionFactory);
    }

    @AfterClass
    public static void tearDownClass() {
        if (sessionFactory != null) sessionFactory.close();
    }

    @Before
    public void setUp() {
        roomDao = new RoomDao();
        seatDao = new SeatDao();
    }

    private CinemaRoom createRoom(String name) {
        CinemaRoom room = new CinemaRoom(name, 100);
        roomDao.insertRoom(room);
        return room;
    }

    private Seat createSeat(CinemaRoom room, String col, int row, String status, String type) {
        Seat seat = new Seat(col, row, status, type);
        seat.setCinemaRoom(room);
        seatDao.insertSeat(seat);
        return seat;
    }

    @Test
    public void testInsertSeat() {
        CinemaRoom room = createRoom("Seat Test Room 1");
        Seat seat = createSeat(room, "A", 1, "Available", "Normal");
        assertNotNull("Seat ID should be generated", seat.getSeatId());
    }

    @Test
    public void testGetSeatById() {
        CinemaRoom room = createRoom("Seat Test Room 2");
        Seat seat = createSeat(room, "B", 2, "Available", "VIP");
        Seat found = seatDao.getSeatById(seat.getSeatId());
        assertNotNull(found);
        assertEquals("B", found.getSeatColumn());
        assertEquals(2, found.getSeatRow());
    }

    @Test
    public void testGetAllSeats() {
        CinemaRoom room = createRoom("Seat Test Room 3");
        createSeat(room, "C", 1, "Available", "Normal");
        createSeat(room, "C", 2, "Booked", "VIP");
        List<Seat> seats = seatDao.getAllSeats();
        assertTrue("Should have at least 2 seats", seats.size() >= 2);
    }

    @Test
    public void testUpdateSeatById() {
        CinemaRoom room = createRoom("Seat Test Room 4");
        Seat seat = createSeat(room, "D", 1, "Available", "Normal");
        boolean updated = seatDao.updateSeatById(seat.getSeatId(), "Booked", "VIP");
        assertTrue(updated);
        Seat found = seatDao.getSeatById(seat.getSeatId());
        assertEquals("Booked", found.getSeatStatus());
        assertEquals("VIP", found.getSeatType());
    }

    @Test
    public void testDeleteSeatById() {
        CinemaRoom room = createRoom("Seat Test Room 5");
        Seat seat = createSeat(room, "E", 1, "Available", "Normal");
        boolean deleted = seatDao.deleteSeatById(seat.getSeatId());
        assertTrue(deleted);
        assertNull(seatDao.getSeatById(seat.getSeatId()));
    }

    @Test
    public void testGetSeatsByRoomAndStatus() {
        CinemaRoom room = createRoom("Seat Test Room 6");
        createSeat(room, "F", 1, "Available", "Normal");
        createSeat(room, "F", 2, "Booked", "Normal");
        createSeat(room, "F", 3, "Available", "VIP");

        List<Seat> available = seatDao.getSeatsByRoomAndStatus(room.getRoomId(), "Available");
        assertEquals("Should find 2 available seats", 2, available.size());
    }

    @Test
    public void testGetSeatsByType() {
        CinemaRoom room = createRoom("Seat Test Room 7");
        createSeat(room, "G", 1, "Available", "VIP");
        createSeat(room, "G", 2, "Available", "VIP");
        createSeat(room, "G", 3, "Available", "Normal");

        List<Seat> vipSeats = seatDao.getSeatsByType("VIP");
        assertTrue("Should find at least 2 VIP seats", vipSeats.size() >= 2);
    }

    @Test
    public void testDeleteNonExistentSeat() {
        boolean deleted = seatDao.deleteSeatById(99999L);
        assertFalse("Should return false for non-existent seat", deleted);
    }
}
