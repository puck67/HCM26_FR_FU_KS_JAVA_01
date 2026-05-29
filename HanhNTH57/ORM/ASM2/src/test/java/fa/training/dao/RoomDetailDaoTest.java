package fa.training.dao;

import fa.training.dao.impl.RoomDaoImpl;
import fa.training.dao.impl.RoomDetailDaoImpl;
import fa.training.entities.CinemaRoom;
import fa.training.entities.CinemaRoomDetail;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RoomDetailDaoTest {

    private static RoomDao roomDao;
    private static RoomDetailDao roomDetailDao;

    @BeforeAll
    static void setUp() {
        roomDao = new RoomDaoImpl();
        roomDetailDao = new RoomDetailDaoImpl();
    }

    @AfterAll
    static void tearDown() {
        // Shared SessionFactory should not be closed here if other tests are running
    }

    private static int insertedId;

    @Test
    @Order(1)
    void testInsertRoomDetail() {
        CinemaRoom room = new CinemaRoom("Room 2", 100);
        roomDao.insertRoom(room);

        CinemaRoomDetail detail = new CinemaRoomDetail(50000, LocalDate.now(), "Premium room");
        detail.setCinemaRoom(room);
        
        assertTrue(roomDetailDao.insertRoomDetail(detail));
        insertedId = detail.getCinemaRoomDetailId();
        assertTrue(insertedId > 0);
    }

    @Test
    @Order(2)
    void testGetRoomDetailById() {
        CinemaRoomDetail detail = roomDetailDao.getRoomDetailById(insertedId);
        assertNotNull(detail);
        assertEquals(50000, detail.getRoomRate());
    }

    @Test
    @Order(3)
    void testGetAllRoomDetails() {
        List<CinemaRoomDetail> details = roomDetailDao.getAllRoomDetails();
        assertFalse(details.isEmpty());
    }

    @Test
    @Order(4)
    void testUpdateRoomDetailById() {
        CinemaRoomDetail detail = new CinemaRoomDetail(60000, LocalDate.now().plusDays(1), "Ultra Premium room");
        assertTrue(roomDetailDao.updateRoomDetailById(insertedId, detail));
        CinemaRoomDetail updatedDetail = roomDetailDao.getRoomDetailById(insertedId);
        assertEquals(60000, updatedDetail.getRoomRate());
    }

    @Test
    @Order(5)
    void testDeleteRoomDetailById() {
        assertTrue(roomDetailDao.deleteRoomDetailById(insertedId));
        assertNull(roomDetailDao.getRoomDetailById(insertedId));
    }
}
