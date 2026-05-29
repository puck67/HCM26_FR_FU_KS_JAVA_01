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
    private static CinemaRoom testRoom;
    private static int testRoomDetailId;

    @BeforeAll
    public static void setup() {
        roomDao = new RoomDaoImpl();
        roomDetailDao = new RoomDetailDaoImpl();
        
        // Setup a temporary cinema room to link detail to
        testRoom = new CinemaRoom("Room Detail Test Room", 80);
        roomDao.insertRoom(testRoom);
    }

    @AfterAll
    public static void tearDown() {
        if (testRoom != null) {
            roomDao.deleteRoomById(testRoom.getCinemaRoomId());
        }
    }

    @Test
    @Order(1)
    public void testInsertRoomDetail() {
        CinemaRoomDetail detail = new CinemaRoomDetail(150000, LocalDate.now(), "Premium IMAX Setup");
        detail.setCinemaRoom(testRoom);

        boolean result = roomDetailDao.insertRoomDetail(detail);
        assertTrue(result, "Room detail should be inserted successfully");
        assertTrue(detail.getCinemaRoomDetailId() > 0, "Generated ID should be set on detail");
        testRoomDetailId = detail.getCinemaRoomDetailId();
    }

    @Test
    @Order(2)
    public void testGetRoomDetailById() {
        CinemaRoomDetail detail = roomDetailDao.getRoomDetailById(testRoomDetailId);
        assertNotNull(detail, "Should find the room detail by ID");
        assertEquals(150000, detail.getRoomRate());
        assertEquals("Premium IMAX Setup", detail.getRoomDescription());
        assertNotNull(detail.getCinemaRoom(), "Associated Cinema Room should not be null");
    }

    @Test
    @Order(3)
    public void testGetAllRoomDetails() {
        List<CinemaRoomDetail> details = roomDetailDao.getAllRoomDetails();
        assertNotNull(details, "Details list should not be null");
        assertTrue(details.size() > 0, "Details list should contain at least our test record");
    }

    @Test
    @Order(4)
    public void testUpdateRoomDetailById() {
        CinemaRoomDetail detail = roomDetailDao.getRoomDetailById(testRoomDetailId);
        assertNotNull(detail);
        detail.setRoomRate(180000);
        detail.setRoomDescription("Premium IMAX Setup with Dolby Atmos");

        boolean result = roomDetailDao.updateRoomDetailById(detail);
        assertTrue(result, "Room detail update should be successful");

        CinemaRoomDetail updated = roomDetailDao.getRoomDetailById(testRoomDetailId);
        assertEquals(180000, updated.getRoomRate());
        assertEquals("Premium IMAX Setup with Dolby Atmos", updated.getRoomDescription());
    }

    @Test
    @Order(5)
    public void testDeleteRoomDetailById() {
        boolean result = roomDetailDao.deleteRoomDetailById(testRoomDetailId);
        assertTrue(result, "Room detail deletion should be successful");

        CinemaRoomDetail deleted = roomDetailDao.getRoomDetailById(testRoomDetailId);
        assertNull(deleted, "Room detail should no longer exist in the database");
    }
}
