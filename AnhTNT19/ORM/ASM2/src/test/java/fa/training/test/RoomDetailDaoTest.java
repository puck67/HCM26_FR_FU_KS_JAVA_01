package fa.training.test;

import fa.training.dao.RoomDao;
import fa.training.dao.RoomDetailDao;
import fa.training.entities.CinemaRoom;
import fa.training.entities.CinemaRoomDetail;
import fa.training.utils.HibernateUtils;
import org.junit.jupiter.api.*;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RoomDetailDaoTest {

    private static RoomDetailDao detailDao;
    private static RoomDao roomDao;
    private static int savedDetailId;
    private static int helperRoomId;

    @BeforeAll
    static void setUp() {
        detailDao = new RoomDetailDao();
        roomDao = new RoomDao();
        CinemaRoom room = new CinemaRoom("Detail Test Room", 10);
        roomDao.insertRoom(room);
        helperRoomId = room.getCinemaRoomId();
    }

    @AfterAll
    static void tearDown() {
        roomDao.deleteRoomById(helperRoomId);
        HibernateUtils.shutdown();
    }

    @Test @Order(1)
    void testInsertDetail() {
        CinemaRoomDetail detail = new CinemaRoomDetail(120000, LocalDate.of(2024, 5, 1), "Test desc");
        detail.setCinemaRoom(roomDao.getRoomById(helperRoomId));
        assertTrue(detailDao.insertDetail(detail));
        savedDetailId = detail.getCinemaRoomDetailId();
    }

    @Test @Order(2)
    void testGetDetailById() {
        CinemaRoomDetail d = detailDao.getDetailById(savedDetailId);
        assertNotNull(d);
        assertEquals(120000, d.getRoomRate());
    }

    @Test @Order(3)
    void testGetAllDetails() {
        assertFalse(detailDao.getAllDetails().isEmpty());
    }

    @Test @Order(4)
    void testUpdateDetailById() {
        assertTrue(detailDao.updateDetailById(savedDetailId, 200000, LocalDate.of(2025, 1, 20), "Updated"));
        assertEquals(200000, detailDao.getDetailById(savedDetailId).getRoomRate());
    }

    @Test @Order(5)
    void testGetDetailsByActiveDateFrom() {
        List<CinemaRoomDetail> results = detailDao.getDetailsByActiveDateFrom(LocalDate.of(2025, 1, 1));
        assertNotNull(results);
        assertTrue(results.stream().anyMatch(d -> d.getCinemaRoomDetailId() == savedDetailId));
    }

    @Test @Order(6)
    void testDeleteDetailById() {
        assertTrue(detailDao.deleteDetailById(savedDetailId));
        assertNull(detailDao.getDetailById(savedDetailId));
    }
}