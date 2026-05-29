package fa.training;

import fa.training.dao.RoomDao;
import fa.training.entities.CinemaRoom;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RoomDaoTest {

    private static final RoomDao dao = new RoomDao();

    @Test
    void saveRoom() {
        CinemaRoom room = new CinemaRoom("SaveTest_" + System.nanoTime(), 40);

        dao.save(room);

        assertTrue(room.getCinemaRoomId() > 0);

        dao.deleteById(room.getCinemaRoomId());
    }

    @Test
    void findRoomById() {
        CinemaRoom room = new CinemaRoom("FindByIdTest_" + System.nanoTime(), 60);
        dao.save(room);

        Optional<CinemaRoom> found = dao.findById(room.getCinemaRoomId());

        assertTrue(found.isPresent());
        assertEquals(room.getCinemaRoomName(), found.get().getCinemaRoomName());
        assertEquals(60, found.get().getSeatQuantity());

        dao.deleteById(room.getCinemaRoomId());
    }

    @Test
    void findAllRooms() {
        CinemaRoom r1 = new CinemaRoom("FindAll_A_" + System.nanoTime(), 20);
        CinemaRoom r2 = new CinemaRoom("FindAll_B_" + System.nanoTime(), 30);
        dao.save(r1);
        dao.save(r2);

        List<CinemaRoom> all = dao.findAll();

        assertTrue(all.size() >= 2);

        dao.deleteById(r1.getCinemaRoomId());
        dao.deleteById(r2.getCinemaRoomId());
    }

    @Test
    void updateRoom() {
        CinemaRoom room = new CinemaRoom("UpdateTest_" + System.nanoTime(), 50);
        dao.save(room);

        room.setSeatQuantity(80);
        dao.update(room);

        Optional<CinemaRoom> found = dao.findById(room.getCinemaRoomId());
        assertTrue(found.isPresent());
        assertEquals(80, found.get().getSeatQuantity());

        dao.deleteById(room.getCinemaRoomId());
    }

    @Test
    void deleteRoomById() {
        CinemaRoom room = new CinemaRoom("DeleteTest_" + System.nanoTime(), 35);
        dao.save(room);
        int id = room.getCinemaRoomId();

        dao.deleteById(id);

        Optional<CinemaRoom> found = dao.findById(id);
        assertFalse(found.isPresent());
    }
}
