package fa.training;

import fa.training.dao.RoomDAO;
import fa.training.entities.CinemaRoom;
import org.junit.jupiter.api.*;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RoomDaoTest {

    private static final RoomDAO dao = new RoomDAO();
    private static int savedRoomId;

    @Test
    @Order(1)
    @DisplayName("Insert a new CinemaRoom")
    void testInsert() {
        CinemaRoom room = new CinemaRoom("Test Room Alpha", 80);
        dao.insert(room);
        assertTrue(room.getCinemaRoomId() > 0, "ID should be generated after insert");
        savedRoomId = room.getCinemaRoomId();
    }

    @Test
    @Order(2)
    @DisplayName("Get CinemaRoom by ID")
    void testGetById() {
        Optional<CinemaRoom> result = dao.getById(savedRoomId);
        assertTrue(result.isPresent(), "Room should be found by id");
        assertEquals("Test Room Alpha", result.get().getCinemaRoomName());
        assertEquals(80, result.get().getSeatQuantity());
    }

    @Test
    @Order(3)
    @DisplayName("Get all CinemaRooms")
    void testGetAll() {
        List<CinemaRoom> rooms = dao.getAll();
        assertFalse(rooms.isEmpty(), "Room list should not be empty");
    }

    @Test
    @Order(4)
    @DisplayName("Update CinemaRoom by ID")
    void testUpdate() {
        Optional<CinemaRoom> opt = dao.getById(savedRoomId);
        assertTrue(opt.isPresent());
        CinemaRoom room = opt.get();
        room.setCinemaRoomName("Test Room Alpha Updated");
        room.setSeatQuantity(100);
        dao.update(room);

        Optional<CinemaRoom> updated = dao.getById(savedRoomId);
        assertTrue(updated.isPresent());
        assertEquals("Test Room Alpha Updated", updated.get().getCinemaRoomName());
        assertEquals(100, updated.get().getSeatQuantity());
    }

    @Test
    @Order(5)
    @DisplayName("Get CinemaRoom by Name")
    void testGetByName() {
        Optional<CinemaRoom> result = dao.getByName("Test Room Alpha Updated");
        assertTrue(result.isPresent(), "Should find room by name");
    }

    @Test
    @Order(6)
    @DisplayName("Delete CinemaRoom by ID")
    void testDeleteById() {
        dao.deleteById(savedRoomId);
        Optional<CinemaRoom> result = dao.getById(savedRoomId);
        assertFalse(result.isPresent(), "Room should not exist after deletion");
    }
}
