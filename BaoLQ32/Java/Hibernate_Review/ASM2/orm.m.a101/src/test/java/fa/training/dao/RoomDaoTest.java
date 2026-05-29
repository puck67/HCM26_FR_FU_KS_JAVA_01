package fa.training.dao;

import fa.training.entities.CinemaRoom;
import fa.training.entities.CinemaRoomDetail;
import fa.training.utils.HibernateUtil;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Integration tests for RoomDao against H2 in-memory database.
 * Each test is independent: fresh room created in setUp, cleaned up in tearDown.
 */
public class RoomDaoTest {

    private static final RoomDao dao = new RoomDao();
    private CinemaRoom testRoom;

    @Before
    public void setUp() {
        CinemaRoom room = new CinemaRoom("Test Room", 10);
        CinemaRoomDetail detail = new CinemaRoomDetail(
                100_000, LocalDate.of(2024, 6, 1), "Test detail");
        room.setDetail(detail);
        testRoom = dao.saveRoom(room);
    }

    @After
    public void tearDown() {
        dao.deleteRoomById(testRoom.getCinemaRoomId());
    }

    @AfterClass
    public static void tearDownClass() {
        HibernateUtil.shutdown();
    }

    @Test
    public void findRoomById_existingId_returnsRoom() {
        Optional<CinemaRoom> result = dao.findRoomById(testRoom.getCinemaRoomId());

        assertTrue("Room should be present", result.isPresent());
        assertEquals("Test Room", result.get().getCinemaRoomName());
        assertEquals(10, result.get().getSeatQuantity());
    }

    @Test
    public void findRoomById_nonExistingId_returnsEmpty() {
        Optional<CinemaRoom> result = dao.findRoomById(Integer.MAX_VALUE);

        assertFalse("Should be empty for unknown id", result.isPresent());
    }

    @Test
    public void findAllRooms_containsAtLeastTestRoom() {
        List<CinemaRoom> rooms = dao.findAllRooms();

        assertNotNull("List must not be null", rooms);
        assertTrue("At least 1 room expected", rooms.size() >= 1);

        boolean found = rooms.stream()
                .anyMatch(r -> r.getCinemaRoomId() == testRoom.getCinemaRoomId());
        assertTrue("Test room must be in list", found);
    }

    @Test
    public void saveRoom_assignsGeneratedId() {
        CinemaRoom room = new CinemaRoom("Save Test Room", 5);
        CinemaRoom saved = dao.saveRoom(room);

        assertTrue("Generated id must be > 0", saved.getCinemaRoomId() > 0);
        assertEquals("Save Test Room", saved.getCinemaRoomName());

        dao.deleteRoomById(saved.getCinemaRoomId()); // cleanup
    }

    @Test
    public void updateRoomById_existingId_updatesFields() {
        boolean updated = dao.updateRoomById(
                testRoom.getCinemaRoomId(), "Updated Hall", 20);

        assertTrue("updateRoomById should return true", updated);

        Optional<CinemaRoom> reloaded = dao.findRoomById(testRoom.getCinemaRoomId());
        assertTrue(reloaded.isPresent());
        assertEquals("Updated Hall", reloaded.get().getCinemaRoomName());
        assertEquals(20, reloaded.get().getSeatQuantity());
    }

    @Test
    public void updateRoomById_nonExistingId_returnsFalse() {
        assertFalse(dao.updateRoomById(Integer.MAX_VALUE, "X", 0));
    }

    @Test
    public void deleteRoomById_existingId_removesRoom() {
        CinemaRoom room = dao.saveRoom(new CinemaRoom("To Delete", 3));
        int id = room.getCinemaRoomId();

        boolean deleted = dao.deleteRoomById(id);

        assertTrue("deleteRoomById should return true", deleted);
        assertFalse("Room should no longer exist",
                dao.findRoomById(id).isPresent());
    }

    @Test
    public void deleteRoomById_nonExistingId_returnsFalse() {
        assertFalse(dao.deleteRoomById(Integer.MAX_VALUE));
    }
}
