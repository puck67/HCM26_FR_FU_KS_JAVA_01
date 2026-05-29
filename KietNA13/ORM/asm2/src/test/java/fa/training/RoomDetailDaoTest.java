package fa.training;

import fa.training.dao.RoomDao;
import fa.training.dao.RoomDetailDao;
import fa.training.entities.CinemaRoom;
import fa.training.entities.CinemaRoomDetail;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RoomDetailDaoTest {

    private static final RoomDetailDao dao = new RoomDetailDao();
    private static final RoomDao roomDao = new RoomDao();
    private static CinemaRoom sharedRoom;

    @BeforeAll
    static void createSharedRoom() {
        sharedRoom = new CinemaRoom("DetailTestRoom_" + System.nanoTime(), 80);
        roomDao.save(sharedRoom);
    }

    @AfterAll
    static void deleteSharedRoom() {
        roomDao.deleteById(sharedRoom.getCinemaRoomId());
    }

    @Test
    void saveDetail() {
        CinemaRoomDetail detail = new CinemaRoomDetail(
                sharedRoom, 150, LocalDate.of(2025, 6, 1), "SaveTest detail"
        );

        dao.save(detail);

        assertTrue(detail.getCinemaRoomDetailId() > 0);

        dao.deleteById(detail.getCinemaRoomDetailId());
    }

    @Test
    void findDetailById() {
        CinemaRoomDetail detail = new CinemaRoomDetail(
                sharedRoom, 120, LocalDate.of(2025, 7, 1), "FindById detail"
        );
        dao.save(detail);

        Optional<CinemaRoomDetail> found = dao.findById(detail.getCinemaRoomDetailId());

        assertTrue(found.isPresent());
        assertEquals(120, found.get().getRoomRate());
        assertEquals(LocalDate.of(2025, 7, 1), found.get().getActiveDate());

        dao.deleteById(detail.getCinemaRoomDetailId());
    }

    @Test
    void findAllDetails() {
        CinemaRoomDetail d = new CinemaRoomDetail(
                sharedRoom, 100, LocalDate.of(2025, 8, 1), "FindAll detail"
        );
        dao.save(d);

        List<CinemaRoomDetail> all = dao.findAll();

        assertFalse(all.isEmpty());

        dao.deleteById(d.getCinemaRoomDetailId());
    }

    @Test
    void updateDetail() {
        CinemaRoomDetail detail = new CinemaRoomDetail(
                sharedRoom, 90, LocalDate.of(2025, 9, 1), "UpdateTest detail"
        );
        dao.save(detail);

        detail.setRoomRate(200);
        detail.setRoomDescription("Updated description");
        dao.update(detail);

        Optional<CinemaRoomDetail> found = dao.findById(detail.getCinemaRoomDetailId());
        assertTrue(found.isPresent());
        assertEquals(200, found.get().getRoomRate());
        assertEquals("Updated description", found.get().getRoomDescription());

        dao.deleteById(detail.getCinemaRoomDetailId());
    }

    @Test
    void deleteDetailById() {
        CinemaRoomDetail detail = new CinemaRoomDetail(
                sharedRoom, 80, LocalDate.of(2025, 10, 1), "DeleteTest detail"
        );
        dao.save(detail);
        int id = detail.getCinemaRoomDetailId();

        dao.deleteById(id);

        Optional<CinemaRoomDetail> found = dao.findById(id);
        assertFalse(found.isPresent());
    }
}
