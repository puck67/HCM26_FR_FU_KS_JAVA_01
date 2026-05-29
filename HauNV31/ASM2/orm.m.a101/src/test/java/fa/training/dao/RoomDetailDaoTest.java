package fa.training.dao;

import fa.training.entities.CinemaRoom;
import fa.training.entities.CinemaRoomDetail;
import fa.training.util.HibernateUtil;
import org.hibernate.SessionFactory;
import org.junit.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class RoomDetailDaoTest {

    private static SessionFactory sessionFactory;
    private RoomDao roomDao;
    private RoomDetailDao roomDetailDao;

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
        roomDetailDao = new RoomDetailDao();
    }

    private CinemaRoom createRoom(String name) {
        CinemaRoom room = new CinemaRoom(name, 50);
        roomDao.insertRoom(room);
        return room;
    }

    @Test
    public void testInsertRoomDetail() {
        CinemaRoom room = createRoom("Detail Test Room 1");
        CinemaRoomDetail detail = new CinemaRoomDetail(150000, LocalDate.of(2024, 1, 1), "Standard room");
        detail.setCinemaRoom(room);
        roomDetailDao.insertRoomDetail(detail);
        assertNotNull("Detail ID should be generated", detail.getDetailId());
    }

    @Test
    public void testGetRoomDetailById() {
        CinemaRoom room = createRoom("Detail Test Room 2");
        CinemaRoomDetail detail = new CinemaRoomDetail(200000, LocalDate.of(2024, 3, 15), "Premium room");
        detail.setCinemaRoom(room);
        roomDetailDao.insertRoomDetail(detail);

        CinemaRoomDetail found = roomDetailDao.getRoomDetailById(detail.getDetailId());
        assertNotNull(found);
        assertEquals(200000, found.getRoomRate(), 0.01);
    }

    @Test
    public void testGetAllRoomDetails() {
        CinemaRoom room1 = createRoom("Detail Test Room 3");
        CinemaRoom room2 = createRoom("Detail Test Room 4");

        CinemaRoomDetail d1 = new CinemaRoomDetail(100000, LocalDate.now(), "Desc 1");
        d1.setCinemaRoom(room1);
        CinemaRoomDetail d2 = new CinemaRoomDetail(120000, LocalDate.now(), "Desc 2");
        d2.setCinemaRoom(room2);

        roomDetailDao.insertRoomDetail(d1);
        roomDetailDao.insertRoomDetail(d2);

        List<CinemaRoomDetail> all = roomDetailDao.getAllRoomDetails();
        assertTrue("Should have at least 2 details", all.size() >= 2);
    }

    @Test
    public void testUpdateRoomDetailById() {
        CinemaRoom room = createRoom("Detail Test Room 5");
        CinemaRoomDetail detail = new CinemaRoomDetail(80000, LocalDate.of(2023, 6, 1), "Old desc");
        detail.setCinemaRoom(room);
        roomDetailDao.insertRoomDetail(detail);

        boolean updated = roomDetailDao.updateRoomDetailById(
                detail.getDetailId(), 95000, LocalDate.of(2024, 6, 1), "New desc");
        assertTrue(updated);

        CinemaRoomDetail found = roomDetailDao.getRoomDetailById(detail.getDetailId());
        assertEquals(95000, found.getRoomRate(), 0.01);
        assertEquals("New desc", found.getDescription());
    }

    @Test
    public void testDeleteRoomDetailById() {
        CinemaRoom room = createRoom("Detail Test Room 6");
        CinemaRoomDetail detail = new CinemaRoomDetail(70000, LocalDate.now(), "To delete");
        detail.setCinemaRoom(room);
        roomDetailDao.insertRoomDetail(detail);

        boolean deleted = roomDetailDao.deleteRoomDetailById(detail.getDetailId());
        assertTrue(deleted);
        assertNull(roomDetailDao.getRoomDetailById(detail.getDetailId()));
    }

    @Test
    public void testGetRoomDetailsByDateRange() {
        CinemaRoom room = createRoom("Detail Test Room 7");
        CinemaRoomDetail detail = new CinemaRoomDetail(60000, LocalDate.of(2024, 5, 10), "Range test");
        detail.setCinemaRoom(room);
        roomDetailDao.insertRoomDetail(detail);

        List<CinemaRoomDetail> results = roomDetailDao.getRoomDetailsByDateRange(
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
        assertFalse("Should find detail within date range", results.isEmpty());
    }
}
