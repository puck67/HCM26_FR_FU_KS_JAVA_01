package fa.training.test;

import fa.training.dao.RoomDao;
import fa.training.entities.CinemaRoom;
import fa.training.utils.HibernateUtils;
import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RoomDaoTest {

    private static RoomDao roomDao;
    private static int savedRoomId;

    @BeforeAll
    static void setUp() { roomDao = new RoomDao(); }

    @AfterAll
    static void tearDown() { HibernateUtils.shutdown(); }

    @Test @Order(1)
    void testInsertRoom() {
        CinemaRoom room = new CinemaRoom("Test Room 1", 20);
        assertTrue(roomDao.insertRoom(room));
        assertTrue(room.getCinemaRoomId() > 0);
        savedRoomId = room.getCinemaRoomId();
    }

    @Test @Order(2)
    void testGetRoomById() {
        CinemaRoom room = roomDao.getRoomById(savedRoomId);
        assertNotNull(room);
        assertEquals("Test Room 1", room.getCinemaRoomName());
    }

    @Test @Order(3)
    void testGetAllRooms() {
        List<CinemaRoom> rooms = roomDao.getAllRooms();
        assertNotNull(rooms);
        assertFalse(rooms.isEmpty());
    }

    @Test @Order(4)
    void testUpdateRoomById() {
        assertTrue(roomDao.updateRoomById(savedRoomId, "Updated Room 1", 35));
        assertEquals("Updated Room 1", roomDao.getRoomById(savedRoomId).getCinemaRoomName());
    }

    @Test @Order(5)
    void testGetRoomByName() {
        assertNotNull(roomDao.getRoomByName("Updated Room 1"));
    }

    @Test @Order(6)
    void testDeleteRoomById() {
        assertTrue(roomDao.deleteRoomById(savedRoomId));
        assertNull(roomDao.getRoomById(savedRoomId));
    }
}