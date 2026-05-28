package fa.training.dao;

import fa.training.dao.impl.RoomDaoImpl;
import fa.training.entities.CinemaRoom;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

import static org.junit.Assert.*;

public class RoomDaoTest extends BaseDaoTest {

    private RoomDao roomDao;

    @Before
    @Override
    public void setUp() {
        super.setUp();
        roomDao = new RoomDaoImpl();
    }

    @Test
    public void testInsertRoom() {
        CinemaRoom room = new CinemaRoom("Room 10 - IMAX 3D", 200);
        boolean result = roomDao.insertRoom(room);
        assertTrue("Insert room should return true", result);
        assertTrue("ID should be generated", room.getCinemaRoomId() > 0);

        CinemaRoom retrieved = roomDao.getRoomById(room.getCinemaRoomId());
        assertNotNull("Retrieved room should not be null", retrieved);
        assertEquals("Name should match", "Room 10 - IMAX 3D", retrieved.getCinemaRoomName());
        assertEquals("Seat quantity should match", 200, retrieved.getSeatQuantity());
    }

    @Test
    public void testGetRoomById() {
        // Test retrieve existing
        CinemaRoom room = new CinemaRoom("Room 02", 120);
        roomDao.insertRoom(room);

        CinemaRoom retrieved = roomDao.getRoomById(room.getCinemaRoomId());
        assertNotNull("Should retrieve room", retrieved);
        assertEquals("Room 02", retrieved.getCinemaRoomName());

        // Test retrieve non-existing
        CinemaRoom nonExisting = roomDao.getRoomById(-999);
        assertNull("Should return null for non-existing ID", nonExisting);
    }

    @Test
    public void testGetAllRooms() {
        CinemaRoom room1 = new CinemaRoom("Room 01", 100);
        CinemaRoom room2 = new CinemaRoom("Room 02", 150);
        roomDao.insertRoom(room1);
        roomDao.insertRoom(room2);

        List<CinemaRoom> rooms = roomDao.getAllRooms();
        assertNotNull("Rooms list should not be null", rooms);
        assertEquals("Should contain 2 rooms", 2, rooms.size());
    }

    @Test
    public void testUpdateRoomById() {
        CinemaRoom room = new CinemaRoom("Room 03 - Deluxe", 80);
        roomDao.insertRoom(room);

        room.setCinemaRoomName("Room 03 - Super Deluxe");
        room.setSeatQuantity(90);
        boolean result = roomDao.updateRoomById(room);
        assertTrue("Update should return true", result);

        CinemaRoom updated = roomDao.getRoomById(room.getCinemaRoomId());
        assertEquals("Name should be updated", "Room 03 - Super Deluxe", updated.getCinemaRoomName());
        assertEquals("Seat quantity should be updated", 90, updated.getSeatQuantity());
    }

    @Test
    public void testDeleteRoomById() {
        CinemaRoom room = new CinemaRoom("Room to Delete", 50);
        roomDao.insertRoom(room);

        boolean result = roomDao.deleteRoomById(room.getCinemaRoomId());
        assertTrue("Delete should return true", result);

        CinemaRoom deleted = roomDao.getRoomById(room.getCinemaRoomId());
        assertNull("Deleted room should not be found", deleted);

        // Delete non-existing room should return false
        boolean resultNonExisting = roomDao.deleteRoomById(-999);
        assertFalse("Delete non-existing should return false", resultNonExisting);
    }
}
