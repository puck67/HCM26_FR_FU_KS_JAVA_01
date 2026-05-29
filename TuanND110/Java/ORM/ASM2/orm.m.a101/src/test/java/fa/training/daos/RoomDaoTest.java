package fa.training.daos;

import fa.training.entities.CinemaRoom;
import fa.training.entities.CinemaRoomDetail;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

/**
 * RoomDaoTest - Unit tests for RoomDao
 */
public class RoomDaoTest {
    private static RoomDao roomDao;

    public RoomDaoTest() {
        roomDao = new RoomDao();
    }

    @Test
    public void testInsertRoom() {
        CinemaRoom room = new CinemaRoom("Cinema Hall 1", 100);
        CinemaRoomDetail detail = new CinemaRoomDetail(200, LocalDate.now(), "VIP Hall");
        room.setRoomDetail(detail);

        Integer roomId = roomDao.insertRoom(room);
        assertNotNull(roomId);
        assertTrue(roomId > 0);
        System.out.println("✓ testInsertRoom passed with ID: " + roomId);
    }

    @Test
    public void testGetRoomById() {
        CinemaRoom room = new CinemaRoom("Cinema Hall 2", 120);
        CinemaRoomDetail detail = new CinemaRoomDetail(250, LocalDate.now(), "Standard Hall");
        room.setRoomDetail(detail);
        Integer roomId = roomDao.insertRoom(room);

        CinemaRoom retrievedRoom = roomDao.getRoomById(roomId);
        assertNotNull(retrievedRoom);
        assertEquals("Cinema Hall 2", retrievedRoom.getCinemaRoomName());
        assertEquals(Integer.valueOf(120), retrievedRoom.getSeatQuantity());
        System.out.println("✓ testGetRoomById passed");
    }

    @Test
    public void testGetAllRooms() {
        for (int i = 1; i <= 3; i++) {
            CinemaRoom room = new CinemaRoom("Cinema Hall Test " + i, 80 + i * 10);
            CinemaRoomDetail detail = new CinemaRoomDetail(150 + i * 50, LocalDate.now(), "Hall " + i);
            room.setRoomDetail(detail);
            roomDao.insertRoom(room);
        }

        List<CinemaRoom> allRooms = roomDao.getAllRooms();
        assertNotNull(allRooms);
        assertTrue(allRooms.size() >= 3);
        System.out.println("✓ testGetAllRooms passed with " + allRooms.size() + " rooms");
    }

    @Test
    public void testGetRoomByName() {
        CinemaRoom room = new CinemaRoom("Premium Cinema", 150);
        CinemaRoomDetail detail = new CinemaRoomDetail(300, LocalDate.now(), "Premium Hall");
        room.setRoomDetail(detail);
        roomDao.insertRoom(room);

        CinemaRoom retrievedRoom = roomDao.getRoomByName("Premium Cinema");
        assertNotNull(retrievedRoom);
        assertEquals("Premium Cinema", retrievedRoom.getCinemaRoomName());
        System.out.println("✓ testGetRoomByName passed");
    }

    @Test
    public void testUpdateRoomById() {
        CinemaRoom room = new CinemaRoom("Old Cinema Name", 90);
        CinemaRoomDetail detail = new CinemaRoomDetail(180, LocalDate.now(), "Old Name");
        room.setRoomDetail(detail);
        Integer roomId = roomDao.insertRoom(room);

        CinemaRoom updatedRoom = new CinemaRoom("New Cinema Name", 110);
        CinemaRoomDetail newDetail = new CinemaRoomDetail(200, LocalDate.now(), "New Name");
        updatedRoom.setRoomDetail(newDetail);
        
        roomDao.updateRoomById(roomId, updatedRoom);

        CinemaRoom resultRoom = roomDao.getRoomById(roomId);
        assertNotNull(resultRoom);
        assertEquals("New Cinema Name", resultRoom.getCinemaRoomName());
        assertEquals(Integer.valueOf(110), resultRoom.getSeatQuantity());
        System.out.println("✓ testUpdateRoomById passed");
    }

    @Test
    public void testDeleteRoomById() {
        CinemaRoom room = new CinemaRoom("Cinema To Delete", 50);
        CinemaRoomDetail detail = new CinemaRoomDetail(150, LocalDate.now(), "To Delete");
        room.setRoomDetail(detail);
        Integer roomId = roomDao.insertRoom(room);

        roomDao.deleteRoomById(roomId);
        CinemaRoom deletedRoom = roomDao.getRoomById(roomId);
        assertNull(deletedRoom);
        System.out.println("✓ testDeleteRoomById passed");
    }

    @Test
    public void testCountAllRooms() {
        Long count = roomDao.countAllRooms();
        assertNotNull(count);
        assertTrue(count >= 0);
        System.out.println("✓ testCountAllRooms passed with count: " + count);
    }

    @Test
    public void testGetRoomWithDetails() {
        CinemaRoom room = new CinemaRoom("Detailed Cinema", 200);
        CinemaRoomDetail detail = new CinemaRoomDetail(220, LocalDate.now(), "Detailed Hall");
        room.setRoomDetail(detail);
        Integer roomId = roomDao.insertRoom(room);

        CinemaRoom retrievedRoom = roomDao.getRoomWithDetails(roomId);
        assertNotNull(retrievedRoom);
        assertNotNull(retrievedRoom.getRoomDetail());
        assertEquals("Detailed Hall", retrievedRoom.getRoomDetail().getRoomDescription());
        System.out.println("✓ testGetRoomWithDetails passed");
    }
}
