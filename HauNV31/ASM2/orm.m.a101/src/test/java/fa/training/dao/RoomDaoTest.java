package fa.training.dao;

import fa.training.entities.CinemaRoom;
import fa.training.entities.CinemaRoomDetail;
import fa.training.entities.Seat;
import fa.training.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class RoomDaoTest {

    private static SessionFactory sessionFactory;
    private RoomDao roomDao;

    @BeforeClass
    public static void setUpClass() {
        // Use test hibernate config (create-drop, separate H2 file)
        sessionFactory = new org.hibernate.cfg.Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();
        // Override HibernateUtil's factory with test factory
        HibernateUtil.setSessionFactory(sessionFactory);
    }

    @AfterClass
    public static void tearDownClass() {
        if (sessionFactory != null) sessionFactory.close();
    }

    @Before
    public void setUp() {
        roomDao = new RoomDao();
    }

    @Test
    public void testInsertRoom() {
        CinemaRoom room = new CinemaRoom("Room A", 100);
        roomDao.insertRoom(room);
        assertNotNull("Room ID should be generated", room.getRoomId());
    }

    @Test
    public void testGetRoomById() {
        CinemaRoom room = new CinemaRoom("Room B", 80);
        roomDao.insertRoom(room);
        CinemaRoom found = roomDao.getRoomById(room.getRoomId());
        assertNotNull(found);
        assertEquals("Room B", found.getRoomName());
    }

    @Test
    public void testGetAllRooms() {
        roomDao.insertRoom(new CinemaRoom("Room C", 60));
        roomDao.insertRoom(new CinemaRoom("Room D", 50));
        List<CinemaRoom> rooms = roomDao.getAllRooms();
        assertTrue("Should have at least 2 rooms", rooms.size() >= 2);
    }

    @Test
    public void testUpdateRoomById() {
        CinemaRoom room = new CinemaRoom("Room E", 70);
        roomDao.insertRoom(room);
        boolean updated = roomDao.updateRoomById(room.getRoomId(), "Room E Updated", 90);
        assertTrue(updated);
        CinemaRoom found = roomDao.getRoomById(room.getRoomId());
        assertEquals("Room E Updated", found.getRoomName());
        assertEquals(90, found.getCapacity());
    }

    @Test
    public void testDeleteRoomById() {
        CinemaRoom room = new CinemaRoom("Room F", 40);
        roomDao.insertRoom(room);
        boolean deleted = roomDao.deleteRoomById(room.getRoomId());
        assertTrue(deleted);
        assertNull(roomDao.getRoomById(room.getRoomId()));
    }

    @Test
    public void testGetRoomsByName() {
        roomDao.insertRoom(new CinemaRoom("VIP Hall", 200));
        List<CinemaRoom> results = roomDao.getRoomsByName("VIP");
        assertFalse("Should find at least one VIP room", results.isEmpty());
    }

    @Test
    public void testUpdateNonExistentRoom() {
        boolean updated = roomDao.updateRoomById(99999L, "Ghost", 0);
        assertFalse("Should return false for non-existent room", updated);
    }
}
