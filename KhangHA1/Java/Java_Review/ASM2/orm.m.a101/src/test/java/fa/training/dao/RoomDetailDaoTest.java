package fa.training.dao;

import fa.training.dao.impl.RoomDaoImpl;
import fa.training.dao.impl.RoomDetailDaoImpl;
import fa.training.entities.CinemaRoom;
import fa.training.entities.CinemaRoomDetail;
import org.junit.Before;
import org.junit.Test;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class RoomDetailDaoTest extends BaseDaoTest {

    private RoomDao roomDao;
    private RoomDetailDao detailDao;

    @Before
    @Override
    public void setUp() {
        super.setUp();
        roomDao = new RoomDaoImpl();
        detailDao = new RoomDetailDaoImpl();
    }

    @Test
    public void testInsertRoomDetail() {
        CinemaRoom room = new CinemaRoom("Detail Room", 80);
        roomDao.insertRoom(room);

        CinemaRoomDetail detail = new CinemaRoomDetail(120000, LocalDate.of(2026, 6, 1), "VIP IMAX theater room");
        detail.setCinemaRoom(room);

        boolean result = detailDao.insertRoomDetail(detail);
        assertTrue("Insert room detail should return true", result);
        assertTrue("ID should be generated", detail.getCinemaRoomDetailId() > 0);

        CinemaRoomDetail retrieved = detailDao.getRoomDetailById(detail.getCinemaRoomDetailId());
        assertNotNull("Retrieved room detail should not be null", retrieved);
        assertEquals(120000, retrieved.getRoomRate());
        assertEquals(LocalDate.of(2026, 6, 1), retrieved.getActiveDate());
        assertEquals("VIP IMAX theater room", retrieved.getRoomDescription());
        assertEquals(room.getCinemaRoomId(), retrieved.getCinemaRoom().getCinemaRoomId());
    }

    @Test
    public void testGetRoomDetailById() {
        CinemaRoom room = new CinemaRoom("Room to detail", 60);
        roomDao.insertRoom(room);

        CinemaRoomDetail detail = new CinemaRoomDetail(90000, LocalDate.of(2026, 5, 29), "Standard details");
        detail.setCinemaRoom(room);
        detailDao.insertRoomDetail(detail);

        CinemaRoomDetail retrieved = detailDao.getRoomDetailById(detail.getCinemaRoomDetailId());
        assertNotNull(retrieved);
        assertEquals("Standard details", retrieved.getRoomDescription());

        CinemaRoomDetail nonExisting = detailDao.getRoomDetailById(-999);
        assertNull(nonExisting);
    }

    @Test
    public void testGetAllRoomDetails() {
        CinemaRoom room1 = new CinemaRoom("Room 1", 70);
        CinemaRoom room2 = new CinemaRoom("Room 2", 90);
        roomDao.insertRoom(room1);
        roomDao.insertRoom(room2);

        CinemaRoomDetail detail1 = new CinemaRoomDetail(100000, LocalDate.now(), "Desc 1");
        detail1.setCinemaRoom(room1);
        CinemaRoomDetail detail2 = new CinemaRoomDetail(200000, LocalDate.now(), "Desc 2");
        detail2.setCinemaRoom(room2);

        detailDao.insertRoomDetail(detail1);
        detailDao.insertRoomDetail(detail2);

        List<CinemaRoomDetail> list = detailDao.getAllRoomDetails();
        assertNotNull(list);
        assertEquals(2, list.size());
    }

    @Test
    public void testUpdateRoomDetailById() {
        CinemaRoom room = new CinemaRoom("Room Update Detail", 100);
        roomDao.insertRoom(room);

        CinemaRoomDetail detail = new CinemaRoomDetail(100000, LocalDate.now(), "Old description");
        detail.setCinemaRoom(room);
        detailDao.insertRoomDetail(detail);

        detail.setRoomRate(130000);
        detail.setRoomDescription("New updated description");
        boolean result = detailDao.updateRoomDetailById(detail);
        assertTrue(result);

        CinemaRoomDetail updated = detailDao.getRoomDetailById(detail.getCinemaRoomDetailId());
        assertEquals(130000, updated.getRoomRate());
        assertEquals("New updated description", updated.getRoomDescription());
    }

    @Test
    public void testDeleteRoomDetailById() {
        CinemaRoom room = new CinemaRoom("Room Delete Detail", 50);
        roomDao.insertRoom(room);

        CinemaRoomDetail detail = new CinemaRoomDetail(85000, LocalDate.now(), "To be deleted");
        detail.setCinemaRoom(room);
        detailDao.insertRoomDetail(detail);

        boolean result = detailDao.deleteRoomDetailById(detail.getCinemaRoomDetailId());
        assertTrue(result);

        CinemaRoomDetail deleted = detailDao.getRoomDetailById(detail.getCinemaRoomDetailId());
        assertNull(deleted);

        // CinemaRoom should still exist
        assertNotNull(roomDao.getRoomById(room.getCinemaRoomId()));
    }

    @Test
    public void testCascadeDeleteRoomDetail() {
        CinemaRoom room = new CinemaRoom("Room Cascade", 50);
        roomDao.insertRoom(room);

        CinemaRoomDetail detail = new CinemaRoomDetail(95000, LocalDate.now(), "Cascade detail test");
        detail.setCinemaRoom(room);
        detailDao.insertRoomDetail(detail);

        int detailId = detail.getCinemaRoomDetailId();
        int roomId = room.getCinemaRoomId();

        // Deleting the CinemaRoom should cascade and delete the detail due to CascadeType.ALL mapping
        boolean deleteResult = roomDao.deleteRoomById(roomId);
        assertTrue("Deleting room should return true", deleteResult);

        assertNull("Room should be deleted", roomDao.getRoomById(roomId));
        assertNull("Detail should be cascade deleted", detailDao.getRoomDetailById(detailId));
    }
}
