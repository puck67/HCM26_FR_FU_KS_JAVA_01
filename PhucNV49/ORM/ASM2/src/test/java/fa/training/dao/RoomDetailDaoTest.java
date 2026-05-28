package fa.training.dao;

import fa.training.dao.impl.RoomDaoImpl;
import fa.training.dao.impl.RoomDetailDaoImpl;
import fa.training.entities.CinemaRoom;
import fa.training.entities.CinemaRoomDetail;
import fa.training.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RoomDetailDaoTest {
    private final RoomDao roomDao = new RoomDaoImpl();
    private final RoomDetailDao roomDetailDao = new RoomDetailDaoImpl();
    private CinemaRoom defaultRoom;

    @BeforeEach
    public void cleanUp() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.createMutationQuery("delete from Seat").executeUpdate();
            session.createMutationQuery("delete from CinemaRoomDetail").executeUpdate();
            session.createMutationQuery("delete from CinemaRoom").executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.err.println("CleanUp failed: " + e.getMessage());
        }

        // Create a default room
        defaultRoom = new CinemaRoom("Room detail test room", 30);
        roomDao.insertRoom(defaultRoom);
    }

    @Test
    public void testInsertRoomDetail() {
        CinemaRoomDetail detail = new CinemaRoomDetail(150000, LocalDate.now(), "Dolby Atmos Setup");
        defaultRoom.setCinemaRoomDetailHelper(detail);

        boolean success = roomDetailDao.insertRoomDetail(detail);
        assertTrue(success);
        assertNotEquals(0, detail.getCinemaRoomDetailId());
    }

    @Test
    public void testGetRoomDetailByID() {
        CinemaRoomDetail detail = new CinemaRoomDetail(200000, LocalDate.of(2026, 5, 28), "IMAX Screen Setup");
        defaultRoom.setCinemaRoomDetailHelper(detail);
        roomDetailDao.insertRoomDetail(detail);

        CinemaRoomDetail fetched = roomDetailDao.getRoomDetailByID(detail.getCinemaRoomDetailId());
        assertNotNull(fetched);
        assertEquals(200000, fetched.getRoomRate());
        assertEquals(LocalDate.of(2026, 5, 28), fetched.getActiveDate());
        assertEquals("IMAX Screen Setup", fetched.getRoomDescription());
    }

    @Test
    public void testGetAllRoomDetail() {
        CinemaRoomDetail detail = new CinemaRoomDetail(90000, LocalDate.now(), "Standard Room Detail");
        defaultRoom.setCinemaRoomDetailHelper(detail);
        roomDetailDao.insertRoomDetail(detail);

        List<CinemaRoomDetail> details = roomDetailDao.getAllRoomDetail();
        assertEquals(1, details.size());
    }

    @Test
    public void testUpdateRoomDetailByID() {
        CinemaRoomDetail detail = new CinemaRoomDetail(100000, LocalDate.now(), "Standard Room");
        defaultRoom.setCinemaRoomDetailHelper(detail);
        roomDetailDao.insertRoomDetail(detail);

        detail.setRoomRate(120000);
        detail.setRoomDescription("Standard Room Upgraded");
        boolean success = roomDetailDao.updateRoomDetailByID(detail);
        assertTrue(success);

        CinemaRoomDetail updated = roomDetailDao.getRoomDetailByID(detail.getCinemaRoomDetailId());
        assertEquals(120000, updated.getRoomRate());
        assertEquals("Standard Room Upgraded", updated.getRoomDescription());
    }

    @Test
    public void testDeleteRoomDetailById() {
        CinemaRoomDetail detail = new CinemaRoomDetail(80000, LocalDate.now(), "Temporary Setup");
        defaultRoom.setCinemaRoomDetailHelper(detail);
        roomDetailDao.insertRoomDetail(detail);

        boolean success = roomDetailDao.deleteRoomDetailById(detail.getCinemaRoomDetailId());
        assertTrue(success);

        CinemaRoomDetail fetched = roomDetailDao.getRoomDetailByID(detail.getCinemaRoomDetailId());
        assertNull(fetched);
    }
}
