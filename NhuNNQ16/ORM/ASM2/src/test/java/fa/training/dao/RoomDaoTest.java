package fa.training.dao;

import fa.training.dao.impl.RoomDaoImpl;
import fa.training.entities.CinemaRoom;
import fa.training.util.HibernateUtil;
import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RoomDaoTest {

    private static RoomDao roomDao;
    private static int testRoomId;

    @BeforeAll
    public static void setup() {
        roomDao = new RoomDaoImpl();
    }

    @Test
    @Order(1)
    public void testInsertRoom() {
        CinemaRoom room = new CinemaRoom("Room Test Alpha", 50);
        boolean result = roomDao.insertRoom(room);
        assertTrue(result, "Room should be inserted successfully");
        assertTrue(room.getCinemaRoomId() > 0, "Generated ID should be set on room");
        testRoomId = room.getCinemaRoomId();
    }

    @Test
    @Order(2)
    public void testGetRoomById() {
        CinemaRoom room = roomDao.getRoomById(testRoomId);
        assertNotNull(room, "Should find the room by ID");
        assertEquals("Room Test Alpha", room.getCinemaRoomName());
        assertEquals(50, room.getSeatQuantity());
    }

    @Test
    @Order(3)
    public void testGetAllRooms() {
        List<CinemaRoom> rooms = roomDao.getAllRooms();
        assertNotNull(rooms, "Rooms list should not be null");
        assertTrue(rooms.size() > 0, "Rooms list should contain at least our test room");
    }

    @Test
    @Order(4)
    public void testUpdateRoomById() {
        CinemaRoom room = roomDao.getRoomById(testRoomId);
        assertNotNull(room);
        room.setCinemaRoomName("Room Test Alpha Updated");
        room.setSeatQuantity(60);
        
        boolean result = roomDao.updateRoomById(room);
        assertTrue(result, "Room update should be successful");

        CinemaRoom updatedRoom = roomDao.getRoomById(testRoomId);
        assertEquals("Room Test Alpha Updated", updatedRoom.getCinemaRoomName());
        assertEquals(60, updatedRoom.getSeatQuantity());
    }

    @Test
    @Order(5)
    public void testDeleteRoomById() {
        boolean result = roomDao.deleteRoomById(testRoomId);
        assertTrue(result, "Room deletion should be successful");

        CinemaRoom deletedRoom = roomDao.getRoomById(testRoomId);
        assertNull(deletedRoom, "Room should no longer exist in the database");
    }
}
