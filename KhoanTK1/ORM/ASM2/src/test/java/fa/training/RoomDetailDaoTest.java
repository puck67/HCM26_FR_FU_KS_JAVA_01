package fa.training;

import fa.training.dao.RoomDAO;
import fa.training.dao.RoomDetailDAO;
import fa.training.entities.CinemaRoom;
import fa.training.entities.CinemaRoomDetail;
import org.junit.jupiter.api.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RoomDetailDaoTest {

    private static final RoomDAO roomDao = new RoomDAO();
    private static final RoomDetailDAO detailDao = new RoomDetailDAO();
    private static int savedRoomId;
    private static int savedDetailId;

    @BeforeAll
    static void setup() {
        CinemaRoom room = new CinemaRoom("Detail Test Room", 50);
        roomDao.insert(room);
        savedRoomId = room.getCinemaRoomId();
    }

    @AfterAll
    static void cleanup() {
        roomDao.deleteById(savedRoomId);
    }

    @Test
    @Order(1)
    @DisplayName("Insert a new CinemaRoomDetail")
    void testInsert() {
        CinemaRoom room = roomDao.getById(savedRoomId).orElseThrow();
        CinemaRoomDetail detail = new CinemaRoomDetail(room, 250, LocalDate.of(2025, 1, 15), "Premium hall");
        detailDao.insert(detail);
        assertTrue(detail.getCinemaRoomDetailId() > 0);
        savedDetailId = detail.getCinemaRoomDetailId();
    }

    @Test
    @Order(2)
    @DisplayName("Get CinemaRoomDetail by ID")
    void testGetById() {
        Optional<CinemaRoomDetail> result = detailDao.getById(savedDetailId);
        assertTrue(result.isPresent());
        assertEquals(250, result.get().getRoomRate());
        assertEquals("Premium hall", result.get().getRoomDescription());
    }

    @Test
    @Order(3)
    @DisplayName("Get all CinemaRoomDetails")
    void testGetAll() {
        List<CinemaRoomDetail> details = detailDao.getAll();
        assertFalse(details.isEmpty());
    }

    @Test
    @Order(4)
    @DisplayName("Get CinemaRoomDetail by Room ID")
    void testGetByRoomId() {
        Optional<CinemaRoomDetail> result = detailDao.getByRoomId(savedRoomId);
        assertTrue(result.isPresent());
    }

    @Test
    @Order(5)
    @DisplayName("Update CinemaRoomDetail by ID")
    void testUpdate() {
        CinemaRoomDetail detail = detailDao.getById(savedDetailId).orElseThrow();
        detail.setRoomRate(300);
        detail.setRoomDescription("Updated description");
        detailDao.update(detail);

        CinemaRoomDetail updated = detailDao.getById(savedDetailId).orElseThrow();
        assertEquals(300, updated.getRoomRate());
        assertEquals("Updated description", updated.getRoomDescription());
    }

    @Test
    @Order(6)
    @DisplayName("Delete CinemaRoomDetail by ID")
    void testDeleteById() {
        detailDao.deleteById(savedDetailId);
        Optional<CinemaRoomDetail> result = detailDao.getById(savedDetailId);
        assertFalse(result.isPresent());
    }
}
