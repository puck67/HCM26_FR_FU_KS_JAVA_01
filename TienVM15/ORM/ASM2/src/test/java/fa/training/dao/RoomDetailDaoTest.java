package fa.training.dao;

import fa.training.dao.impl.RoomDaoImpl;
import fa.training.dao.impl.RoomDetailDaoImpl;
import fa.training.entities.CinemaRoom;
import fa.training.entities.CinemaRoomDetail;
import fa.training.util.HibernateUtil;
import org.hibernate.Session;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RoomDetailDaoTest {

    private RoomDao roomDao;
    private RoomDetailDao detailDao;

    @BeforeAll
    public static void setUpClass() {
        System.setProperty("hibernate.config.file", "hibernate-test.cfg.xml");
        HibernateUtil.getSessionFactory();
    }

    @BeforeEach
    public void setUp() {
        roomDao = new RoomDaoImpl();
        detailDao = new RoomDetailDaoImpl();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.createMutationQuery("DELETE FROM CinemaRoomDetail").executeUpdate();
            session.createMutationQuery("DELETE FROM Seat").executeUpdate();
            session.createMutationQuery("DELETE FROM CinemaRoom").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test
    public void testInsertAndGetRoomDetailByID() {
        CinemaRoom room = new CinemaRoom("Room 1", 50);
        roomDao.insertRoom(room);

        CinemaRoomDetail detail = new CinemaRoomDetail(120000, LocalDate.of(2026, 5, 27), "Standard Room Detail");
        detail.setCinemaRoom(room);
        detailDao.insertRoomDetail(detail);

        assertTrue(detail.getCinemaRoomDetailId() > 0);

        CinemaRoomDetail fetched = detailDao.getRoomDetailByID(detail.getCinemaRoomDetailId());
        assertNotNull(fetched);
        assertEquals(120000, fetched.getRoomRate());
        assertEquals(LocalDate.of(2026, 5, 27), fetched.getActiveDate());
        assertEquals("Standard Room Detail", fetched.getRoomDescription());
        assertEquals("Room 1", fetched.getCinemaRoom().getCinemaRoomName());
    }

    @Test
    public void testUpdateRoomDetailByID() {
        CinemaRoom room = new CinemaRoom("Room 1", 50);
        roomDao.insertRoom(room);

        CinemaRoomDetail detail = new CinemaRoomDetail(100000, LocalDate.now(), "Original Detail");
        detail.setCinemaRoom(room);
        detailDao.insertRoomDetail(detail);

        detail.setRoomRate(130000);
        detail.setRoomDescription("Updated Detail");
        detailDao.updateRoomDetailByID(detail);

        CinemaRoomDetail fetched = detailDao.getRoomDetailByID(detail.getCinemaRoomDetailId());
        assertNotNull(fetched);
        assertEquals(130000, fetched.getRoomRate());
        assertEquals("Updated Detail", fetched.getRoomDescription());
    }

    @Test
    public void testDeleteRoomDetailById() {
        CinemaRoom room = new CinemaRoom("Room 1", 50);
        roomDao.insertRoom(room);

        CinemaRoomDetail detail = new CinemaRoomDetail(100000, LocalDate.now(), "Original Detail");
        detail.setCinemaRoom(room);
        detailDao.insertRoomDetail(detail);

        int detailId = detail.getCinemaRoomDetailId();
        detailDao.deleteRoomDetailById(detailId);

        assertNull(detailDao.getRoomDetailByID(detailId));
    }

    @Test
    public void testGetAllRoomDetails() {
        CinemaRoom room1 = new CinemaRoom("Room 1", 30);
        CinemaRoom room2 = new CinemaRoom("Room 2", 40);
        roomDao.insertRoom(room1);
        roomDao.insertRoom(room2);

        CinemaRoomDetail d1 = new CinemaRoomDetail(80000, LocalDate.now(), "D1");
        d1.setCinemaRoom(room1);
        CinemaRoomDetail d2 = new CinemaRoomDetail(90000, LocalDate.now(), "D2");
        d2.setCinemaRoom(room2);

        detailDao.insertRoomDetail(d1);
        detailDao.insertRoomDetail(d2);

        List<CinemaRoomDetail> list = detailDao.getAllRoomDetails();
        assertEquals(2, list.size());
    }
}
