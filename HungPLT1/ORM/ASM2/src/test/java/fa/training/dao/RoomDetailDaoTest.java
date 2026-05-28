package fa.training.dao;

import fa.training.dao.impl.RoomDaoImpl;
import fa.training.dao.impl.RoomDetailDaoImpl;
import fa.training.entities.CinemaRoom;
import fa.training.entities.CinemaRoomDetail;
import fa.training.utils.HibernateUtil;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RoomDetailDaoTest {

    private RoomDao roomDao;
    private RoomDetailDao roomDetailDao;

    @BeforeEach
    public void setUp() {
         roomDao = new RoomDaoImpl();
         roomDetailDao = new RoomDetailDaoImpl();
    }

    @Test
    public void testCinemaRoomDetailCRUD() {
        // Create Room first
        CinemaRoom room = new CinemaRoom("Room 103", 80);
        roomDao.insertRoom(room);

        // Create Detail
        LocalDate activeDate = LocalDate.of(2026, 5, 28);
        CinemaRoomDetail detail = new CinemaRoomDetail(80000, activeDate, "Standard 2D Cinema Room");
        detail.setCinemaRoom(room);
        assertTrue(roomDetailDao.insertRoomDetail(detail), "Should insert CinemaRoomDetail successfully");
        assertTrue(detail.getCinemaRoomDetailId() > 0);

        // Read
        CinemaRoomDetail fetched = roomDetailDao.getRoomDetailById(detail.getCinemaRoomDetailId());
        assertNotNull(fetched);
        assertEquals(80000, fetched.getRoomRate());
        assertEquals(activeDate, fetched.getActiveDate());
        assertEquals("Standard 2D Cinema Room", fetched.getRoomDescription());

        // Update
        fetched.setRoomRate(90000);
        fetched.setRoomDescription("Standard 2D Room with upgraded speakers");
        assertTrue(roomDetailDao.updateRoomDetailById(fetched), "Should update CinemaRoomDetail successfully");

        CinemaRoomDetail updated = roomDetailDao.getRoomDetailById(detail.getCinemaRoomDetailId());
        assertEquals(90000, updated.getRoomRate());
        assertEquals("Standard 2D Room with upgraded speakers", updated.getRoomDescription());

        // Delete
        assertTrue(roomDetailDao.deleteRoomDetailById(detail.getCinemaRoomDetailId()), "Should delete CinemaRoomDetail successfully");
        assertNull(roomDetailDao.getRoomDetailById(detail.getCinemaRoomDetailId()));
    }
}
