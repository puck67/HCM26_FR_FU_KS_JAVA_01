package fa.training.dao;

import fa.training.dao.impl.RoomDaoImpl;
import fa.training.entities.CinemaRoom;
import fa.training.utils.HibernateUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RoomDaoTest {

    private RoomDao roomDao;

    @BeforeEach
    public void setUp() {
        roomDao = new RoomDaoImpl();
    }

    @Test
    public void testCinemaRoomCRUD() {
        // Create
        CinemaRoom room = new CinemaRoom("Room 101", 100);
        assertTrue(roomDao.insertRoom(room), "Should insert CinemaRoom successfully");
        assertTrue(room.getCinemaRoomId() > 0, "ID should be generated");

        // Read
        CinemaRoom fetched = roomDao.getRoomById(room.getCinemaRoomId());
        assertNotNull(fetched, "Should fetch CinemaRoom successfully");
        assertEquals("Room 101", fetched.getCinemaRoomName());
        assertEquals(100, fetched.getSeatQuantity());

        // Update
        fetched.setCinemaRoomName("Room 101 Updated");
        fetched.setSeatQuantity(120);
        assertTrue(roomDao.updateRoomById(fetched), "Should update CinemaRoom successfully");

        CinemaRoom updated = roomDao.getRoomById(room.getCinemaRoomId());
        assertEquals("Room 101 Updated", updated.getCinemaRoomName());
        assertEquals(120, updated.getSeatQuantity());

        // Delete
        assertTrue(roomDao.deleteRoomById(room.getCinemaRoomId()), "Should delete CinemaRoom successfully");
        assertNull(roomDao.getRoomById(room.getCinemaRoomId()), "Deleted Room should not exist");
    }
}
