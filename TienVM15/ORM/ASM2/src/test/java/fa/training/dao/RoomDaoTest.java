package fa.training.dao;

import fa.training.dao.impl.RoomDaoImpl;
import fa.training.entities.CinemaRoom;
import fa.training.entities.Seat;
import fa.training.entities.CinemaRoomDetail;
import fa.training.util.HibernateUtil;
import org.hibernate.Session;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RoomDaoTest {

    private RoomDao roomDao;

    @BeforeAll
    public static void setUpClass() {
        System.setProperty("hibernate.config.file", "hibernate-test.cfg.xml");
        HibernateUtil.getSessionFactory();
    }

    @BeforeEach
    public void setUp() {
        roomDao = new RoomDaoImpl();
        // Clean slate
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.createMutationQuery("DELETE FROM CinemaRoomDetail").executeUpdate();
            session.createMutationQuery("DELETE FROM Seat").executeUpdate();
            session.createMutationQuery("DELETE FROM CinemaRoom").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test
    public void testInsertAndGetRoomByID() {
        CinemaRoom room = new CinemaRoom("Gold Class 1", 30);
        roomDao.insertRoom(room);

        assertTrue(room.getCinemaRoomId() > 0);

        CinemaRoom fetched = roomDao.getRoomByID(room.getCinemaRoomId());
        assertNotNull(fetched);
        assertEquals("Gold Class 1", fetched.getCinemaRoomName());
        assertEquals(30, fetched.getSeatQuantity());
    }

    @Test
    public void testUpdateRoomByID() {
        CinemaRoom room = new CinemaRoom("Standard 2", 50);
        roomDao.insertRoom(room);

        room.setCinemaRoomName("Standard 2 Updated");
        room.setSeatQuantity(60);
        roomDao.updateRoomByID(room);

        CinemaRoom fetched = roomDao.getRoomByID(room.getCinemaRoomId());
        assertEquals("Standard 2 Updated", fetched.getCinemaRoomName());
        assertEquals(60, fetched.getSeatQuantity());
    }

    @Test
    public void testDeleteRoomById() {
        CinemaRoom room = new CinemaRoom("Temporary Room", 10);
        roomDao.insertRoom(room);

        int id = room.getCinemaRoomId();
        roomDao.deleteRoomById(id);

        assertNull(roomDao.getRoomByID(id));
    }

    @Test
    public void testGetAllRooms() {
        roomDao.insertRoom(new CinemaRoom("Room A", 20));
        roomDao.insertRoom(new CinemaRoom("Room B", 40));

        List<CinemaRoom> list = roomDao.getAllRooms();
        assertEquals(2, list.size());
    }

    @Test
    public void testRoomCascadeAll() {
        CinemaRoom room = new CinemaRoom("IMAX 3D", 100);
        
        // Add details
        CinemaRoomDetail detail = new CinemaRoomDetail(150000, LocalDate.now(), "Premium IMAX");
        room.setCinemaRoomDetail(detail);

        // Add seats
        room.addSeat(new Seat("F", 10, "Available", "VIP"));
        room.addSeat(new Seat("F", 11, "Booked", "VIP"));

        roomDao.insertRoom(room);

        // Verify save cascaded to Details and Seats
        CinemaRoom fetched = roomDao.getRoomByID(room.getCinemaRoomId());
        assertNotNull(fetched.getCinemaRoomDetail());
        assertEquals(150000, fetched.getCinemaRoomDetail().getRoomRate());
        assertEquals(2, fetched.getSeats().size());
    }
}
