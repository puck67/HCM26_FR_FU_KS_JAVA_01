package fa.training.dao;

import fa.training.entities.CinemaRoom;
import fa.training.utils.HibernateUtil;
import org.junit.*;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for RoomDAO.
 * Tests all 5 CRUD methods: insert, getByID, getAll, updateByID, deleteByID.
 */
public class RoomDAOTest {

    private static RoomDAO roomDAO;

    @BeforeClass
    public static void setUp() {
        roomDAO = new RoomDAO();
    }

    @AfterClass
    public static void tearDown() {
        HibernateUtil.shutdown();
    }

    // ---- Test insertRoom & getRoomByID ----

    @Test
    public void testInsertAndGetRoomByID() {
        CinemaRoom room = new CinemaRoom("Test Hall Insert", 50);
        roomDAO.insertRoom(room);

        int generatedId = room.getCinemaRoomId();
        assertTrue("ID should be > 0 after insert", generatedId > 0);

        CinemaRoom fetched = roomDAO.getRoomByID(generatedId);
        assertNotNull("Fetched room should not be null", fetched);
        assertEquals("Test Hall Insert", fetched.getCinemaRoomName());
        assertEquals(50, fetched.getSeatQuantity());

        // cleanup
        roomDAO.deleteRoomByID(generatedId);
    }

    // ---- Test getAllRooms ----

    @Test
    public void testGetAllRooms() {
        CinemaRoom room1 = new CinemaRoom("GetAll Room 1", 30);
        CinemaRoom room2 = new CinemaRoom("GetAll Room 2", 40);
        roomDAO.insertRoom(room1);
        roomDAO.insertRoom(room2);

        List<CinemaRoom> rooms = roomDAO.getAllRooms();
        assertNotNull("List should not be null", rooms);
        assertTrue("Should have at least 2 rooms", rooms.size() >= 2);

        // cleanup
        roomDAO.deleteRoomByID(room1.getCinemaRoomId());
        roomDAO.deleteRoomByID(room2.getCinemaRoomId());
    }

    // ---- Test updateRoomByID ----

    @Test
    public void testUpdateRoomByID() {
        CinemaRoom room = new CinemaRoom("Old Name", 10);
        roomDAO.insertRoom(room);
        int id = room.getCinemaRoomId();

        roomDAO.updateRoomByID(id, "New Name", 99);

        CinemaRoom updated = roomDAO.getRoomByID(id);
        assertNotNull(updated);
        assertEquals("New Name", updated.getCinemaRoomName());
        assertEquals(99, updated.getSeatQuantity());

        // cleanup
        roomDAO.deleteRoomByID(id);
    }

    // ---- Test deleteRoomByID ----

    @Test
    public void testDeleteRoomByID() {
        CinemaRoom room = new CinemaRoom("Room To Delete", 5);
        roomDAO.insertRoom(room);
        int id = room.getCinemaRoomId();

        roomDAO.deleteRoomByID(id);

        CinemaRoom deleted = roomDAO.getRoomByID(id);
        assertNull("Room should be null after deletion", deleted);
    }

    // ---- Test getRoomByID with non-existent ID ----

    @Test
    public void testGetRoomByID_NotFound() {
        CinemaRoom result = roomDAO.getRoomByID(999999);
        assertNull("Should return null for non-existent ID", result);
    }
}
