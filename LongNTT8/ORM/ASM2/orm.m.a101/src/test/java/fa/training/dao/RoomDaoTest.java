package fa.training.dao;

import fa.training.entities.CinemaRoom;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class RoomDaoTest {

    private RoomDao roomDao;
    private CinemaRoom testRoom;

    @BeforeEach
    public void setUp() {
        roomDao = new RoomDao();
        testRoom = new CinemaRoom("Test Room", 100);
        // Insert a base room to be used across tests that require an existing entity
        roomDao.insert(testRoom);
    }

    @AfterEach
    public void tearDown() {
        if (testRoom != null && testRoom.getCinemaRoomId() != null) {
            roomDao.deleteById(testRoom.getCinemaRoomId());
        }
    }

    @Test
    public void testInsert() {
        CinemaRoom room = new CinemaRoom("New Room", 50);
        boolean result = roomDao.insert(room);
        assertTrue(result, "Insert should return true");
        assertNotNull(room.getCinemaRoomId(), "Cinema room ID should be generated");

        // Clean up
        roomDao.deleteById(room.getCinemaRoomId());
    }

    @Test
    public void testGetById() {
        CinemaRoom retrievedRoom = roomDao.getById(testRoom.getCinemaRoomId());
        assertNotNull(retrievedRoom, "Retrieved room should not be null");
        assertEquals("Test Room", retrievedRoom.getCinemaRoomName(), "Room name should match");
        assertEquals(100, retrievedRoom.getSeatQuantity(), "Seat quantity should match");
    }

    @Test
    public void testUpdateById() {
        testRoom.setCinemaRoomName("Updated Room");
        testRoom.setSeatQuantity(150);
        boolean updateResult = roomDao.updateById(testRoom);
        assertTrue(updateResult, "Update should return true");

        CinemaRoom retrievedRoom = roomDao.getById(testRoom.getCinemaRoomId());
        assertEquals("Updated Room", retrievedRoom.getCinemaRoomName(), "Room name should be updated");
        assertEquals(150, retrievedRoom.getSeatQuantity(), "Seat quantity should be updated");
    }

    @Test
    public void testDeleteById() {
        CinemaRoom roomToDelete = new CinemaRoom("Delete Me", 10);
        roomDao.insert(roomToDelete);
        Integer id = roomToDelete.getCinemaRoomId();
        
        boolean deleteResult = roomDao.deleteById(id);
        assertTrue(deleteResult, "Delete should return true");

        CinemaRoom retrievedRoom = roomDao.getById(id);
        assertNull(retrievedRoom, "Deleted room should not be found");
    }

    @Test
    public void testGetAll() {
        List<CinemaRoom> rooms = roomDao.getAll();
        assertNotNull(rooms, "List of rooms should not be null");
        assertFalse(rooms.isEmpty(), "List of rooms should not be empty");
    }
}
