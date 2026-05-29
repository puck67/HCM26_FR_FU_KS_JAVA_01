package fa.training.dao;

import fa.training.dao.impl.RoomDaoImpl;
import fa.training.entities.CinemaRoom;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RoomDaoTest {

    private static RoomDao roomDao;

    @BeforeAll
    static void setUp() {
        roomDao = new RoomDaoImpl();
    }

    @AfterAll
    static void tearDown() {
        // Shared SessionFactory should not be closed here if other tests are running
    }

    private static int insertedId;

    @Test
    @Order(1)
    void testInsertRoom() {
        CinemaRoom room = new CinemaRoom("Room 1", 50);
        assertTrue(roomDao.insertRoom(room));
        insertedId = room.getCinemaRoomId();
        assertTrue(insertedId > 0);
    }

    @Test
    @Order(2)
    void testGetRoomById() {
        CinemaRoom room = roomDao.getRoomById(insertedId);
        assertNotNull(room);
        assertEquals("Room 1", room.getCinemaRoomName());
    }

    @Test
    @Order(3)
    void testGetAllRooms() {
        List<CinemaRoom> rooms = roomDao.getAllRooms();
        assertFalse(rooms.isEmpty());
    }

    @Test
    @Order(4)
    void testUpdateRoomById() {
        CinemaRoom room = new CinemaRoom("Room 1 Updated", 60);
        assertTrue(roomDao.updateRoomById(insertedId, room));
        CinemaRoom updatedRoom = roomDao.getRoomById(insertedId);
        assertEquals("Room 1 Updated", updatedRoom.getCinemaRoomName());
        assertEquals(60, updatedRoom.getSeatQuantity());
    }

    @Test
    @Order(5)
    void testDeleteRoomById() {
        assertTrue(roomDao.deleteRoomById(insertedId));
        assertNull(roomDao.getRoomById(insertedId));
    }
}
