package fa.training.dao;

import fa.training.entities.CinemaRoom;
import fa.training.entities.CinemaRoomDetail;
import fa.training.utils.HibernateUtil;
import org.junit.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for RoomDetailDAO.
 * Tests all 5 CRUD methods.
 */
public class RoomDetailDAOTest {

    private static RoomDetailDAO roomDetailDAO;
    private static RoomDAO roomDAO;

    @BeforeClass
    public static void setUp() {
        roomDetailDAO = new RoomDetailDAO();
        roomDAO = new RoomDAO();
    }

    @AfterClass
    public static void tearDown() {
        HibernateUtil.shutdown();
    }

    /**
     * Helper: create a fresh room without a detail, save it, return it.
     */
    private CinemaRoom createTestRoom(String name) {
        CinemaRoom room = new CinemaRoom(name, 20);
        roomDAO.insertRoom(room);
        return room;
    }

    // ---- Test insertRoomDetail & getRoomDetailByID ----

    @Test
    public void testInsertAndGetRoomDetailByID() {
        CinemaRoom room = createTestRoom("Detail Insert Room");

        CinemaRoomDetail detail = new CinemaRoomDetail(
                120_000, LocalDate.of(2024, 3, 1), "Test description");
        detail.setCinemaRoom(room);
        roomDetailDAO.insertRoomDetail(detail);

        int id = detail.getCinemaRoomDetailId();
        assertTrue("ID should be > 0 after insert", id > 0);

        CinemaRoomDetail fetched = roomDetailDAO.getRoomDetailByID(id);
        assertNotNull("Fetched detail should not be null", fetched);
        assertEquals(120_000, fetched.getRoomRate());
        assertEquals(LocalDate.of(2024, 3, 1), fetched.getActiveDate());
        assertEquals("Test description", fetched.getRoomDescription());

        // cleanup
        roomDetailDAO.deleteRoomDetailByID(id);
        roomDAO.deleteRoomByID(room.getCinemaRoomId());
    }

    // ---- Test getAllRoomDetails ----

    @Test
    public void testGetAllRoomDetails() {
        CinemaRoom room1 = createTestRoom("GetAll Detail Room 1");
        CinemaRoom room2 = createTestRoom("GetAll Detail Room 2");

        CinemaRoomDetail d1 = new CinemaRoomDetail(100_000, LocalDate.now(), "Desc 1");
        d1.setCinemaRoom(room1);
        CinemaRoomDetail d2 = new CinemaRoomDetail(200_000, LocalDate.now(), "Desc 2");
        d2.setCinemaRoom(room2);

        roomDetailDAO.insertRoomDetail(d1);
        roomDetailDAO.insertRoomDetail(d2);

        List<CinemaRoomDetail> all = roomDetailDAO.getAllRoomDetails();
        assertNotNull("List should not be null", all);
        assertTrue("Should have at least 2 details", all.size() >= 2);

        // cleanup
        roomDetailDAO.deleteRoomDetailByID(d1.getCinemaRoomDetailId());
        roomDetailDAO.deleteRoomDetailByID(d2.getCinemaRoomDetailId());
        roomDAO.deleteRoomByID(room1.getCinemaRoomId());
        roomDAO.deleteRoomByID(room2.getCinemaRoomId());
    }

    // ---- Test updateRoomDetailByID ----

    @Test
    public void testUpdateRoomDetailByID() {
        CinemaRoom room = createTestRoom("Update Detail Room");

        CinemaRoomDetail detail = new CinemaRoomDetail(50_000, LocalDate.of(2023, 1, 1), "Old desc");
        detail.setCinemaRoom(room);
        roomDetailDAO.insertRoomDetail(detail);
        int id = detail.getCinemaRoomDetailId();

        LocalDate newDate = LocalDate.of(2025, 12, 31);
        roomDetailDAO.updateRoomDetailByID(id, 300_000, newDate, "New desc");

        CinemaRoomDetail updated = roomDetailDAO.getRoomDetailByID(id);
        assertNotNull(updated);
        assertEquals(300_000, updated.getRoomRate());
        assertEquals(newDate, updated.getActiveDate());
        assertEquals("New desc", updated.getRoomDescription());

        // cleanup
        roomDetailDAO.deleteRoomDetailByID(id);
        roomDAO.deleteRoomByID(room.getCinemaRoomId());
    }

    // ---- Test deleteRoomDetailByID ----

    @Test
    public void testDeleteRoomDetailByID() {
        CinemaRoom room = createTestRoom("Delete Detail Room");

        CinemaRoomDetail detail = new CinemaRoomDetail(75_000, LocalDate.now(), "To be deleted");
        detail.setCinemaRoom(room);
        roomDetailDAO.insertRoomDetail(detail);
        int id = detail.getCinemaRoomDetailId();

        roomDetailDAO.deleteRoomDetailByID(id);

        CinemaRoomDetail deleted = roomDetailDAO.getRoomDetailByID(id);
        assertNull("Detail should be null after deletion", deleted);

        // cleanup room
        roomDAO.deleteRoomByID(room.getCinemaRoomId());
    }

    // ---- Test getRoomDetailByID - not found ----

    @Test
    public void testGetRoomDetailByID_NotFound() {
        CinemaRoomDetail result = roomDetailDAO.getRoomDetailByID(999999);
        assertNull("Should return null for non-existent ID", result);
    }
}
