package fa.training.dao;

import fa.training.dao.impl.RoomDaoImpl;
import fa.training.entities.CinemaRoom;
import fa.training.utils.HibernateUtils;
import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class RoomDaoTest {

    private static RoomDao roomDao;

    @BeforeAll
    public static void setUpClass() {
        roomDao = new RoomDaoImpl();
    }

    @AfterAll
    public static void tearDownClass() {
        HibernateUtils.shutdown();
    }

    @BeforeEach
    public void cleanDatabase() {
        List<CinemaRoom> rooms = roomDao.getAllRoom();
        for (CinemaRoom r : rooms) {
            roomDao.deleteRoomById(r.getCinemaRoomId());
        }
    }

    @Test
    public void testInsertRoom_Success() {
        CinemaRoom room = new CinemaRoom("Premium Room 101", 100);
        boolean result = roomDao.insertRoom(room);
        assertTrue(result, new StringBuilder()
                .append("Failed to insert cinema room. Name: ")
                .append(room.getCinemaRoomName())
                .toString());
        assertNotNull(room.getCinemaRoomId(), new StringBuilder()
                .append("Auto-generated room ID should not be null.")
                .toString());
    }

    @Test
    public void testInsertRoom_DuplicateName_ShouldFail() {
        CinemaRoom room1 = new CinemaRoom("Room Dup", 50);
        boolean result1 = roomDao.insertRoom(room1);
        assertTrue(result1);

        CinemaRoom room2 = new CinemaRoom("Room Dup", 80);
        boolean result2 = roomDao.insertRoom(room2);
        assertFalse(result2, new StringBuilder()
                .append("Should fail when inserting duplicate room name: ")
                .append(room2.getCinemaRoomName())
                .toString());
    }

    @Test
    public void testInsertRoom_NullName_ShouldFail() {
        CinemaRoom room = new CinemaRoom(null, 50);
        boolean result = roomDao.insertRoom(room);
        assertFalse(result, new StringBuilder()
                .append("Should fail when inserting room with null name.")
                .toString());
    }

    @Test
    public void testGetRoomById_Existing() {
        CinemaRoom room = new CinemaRoom("Standard Room 02", 60);
        roomDao.insertRoom(room);
        Integer id = room.getCinemaRoomId();

        CinemaRoom fetched = roomDao.getRoomByID(id);
        assertNotNull(fetched);
        assertEquals("Standard Room 02", fetched.getCinemaRoomName());
        assertEquals(60, fetched.getSeatQuantity());
    }

    @Test
    public void testGetRoomById_NotExisting_ShouldReturnNull() {
        CinemaRoom fetched = roomDao.getRoomByID(9999);
        assertNull(fetched, new StringBuilder()
                .append("Getting a non-existing room should return null, not crash.")
                .toString());
    }

    @Test
    public void testGetAllRooms() {
        CinemaRoom room1 = new CinemaRoom("Room A", 40);
        CinemaRoom room2 = new CinemaRoom("Room B", 50);
        roomDao.insertRoom(room1);
        roomDao.insertRoom(room2);

        List<CinemaRoom> rooms = roomDao.getAllRoom();
        assertEquals(2, rooms.size(), new StringBuilder()
                .append("Expected exactly 2 rooms, but found: ")
                .append(rooms.size())
                .toString());
    }

    @Test
    public void testUpdateRoom_Success() {
        CinemaRoom room = new CinemaRoom("Update Test Room", 40);
        roomDao.insertRoom(room);
        Integer id = room.getCinemaRoomId();

        CinemaRoom updateData = new CinemaRoom("Update Test Room", 99);
        boolean result = roomDao.updateRoomByID(id, updateData);
        assertTrue(result);

        CinemaRoom fetched = roomDao.getRoomByID(id);
        assertEquals(99, fetched.getSeatQuantity(), new StringBuilder()
                .append("Expected seat quantity to be updated to 99, but got ")
                .append(fetched.getSeatQuantity())
                .toString());
    }

    @Test
    public void testDeleteRoomById_Success() {
        CinemaRoom room = new CinemaRoom("Delete Room", 30);
        roomDao.insertRoom(room);
        Integer id = room.getCinemaRoomId();

        boolean result = roomDao.deleteRoomById(id);
        assertTrue(result);

        CinemaRoom fetched = roomDao.getRoomByID(id);
        assertNull(fetched, new StringBuilder()
                .append("Deleted room should not exist in the database.")
                .toString());
    }

    @Test
    public void testInsertRoom_Null_ShouldFail() {
        boolean result = roomDao.insertRoom(null);
        assertFalse(result, new StringBuilder()
                .append("Inserting null room must return false, not crash.")
                .toString());
    }

    @Test
    public void testGetRoomById_InvalidId_ShouldReturnNull() {
        CinemaRoom fetched = roomDao.getRoomByID(-5);
        assertNull(fetched, new StringBuilder()
                .append("Invalid negative ID must return null, not crash.")
                .toString());
    }

    @Test
    public void testUpdateRoom_Null_ShouldFail() {
        boolean result = roomDao.updateRoomByID(1, null);
        assertFalse(result, new StringBuilder()
                .append("Updating with null room data must return false, not crash.")
                .toString());
    }

    @Test
    public void testUpdateRoom_InvalidId_ShouldFail() {
        CinemaRoom room = new CinemaRoom("Valid Name", 50);
        boolean result = roomDao.updateRoomByID(-1, room);
        assertFalse(result, new StringBuilder()
                .append("Updating with negative ID must return false, not crash.")
                .toString());
    }

    @Test
    public void testDeleteRoomById_InvalidId_ShouldFail() {
        boolean result = roomDao.deleteRoomById(-10);
        assertFalse(result, new StringBuilder()
                .append("Deleting with negative ID must return false, not crash.")
                .toString());
    }
}
