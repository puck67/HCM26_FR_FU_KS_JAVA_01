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
 * Integration tests for RoomDetailDao.
 * Detail is created via CinemaRoom cascade, then managed independently.
 */
public class RoomDetailDaoTest {

    private static final RoomDao roomDao       = new RoomDao();
    private static final RoomDetailDao detailDao = new RoomDetailDao();

    private CinemaRoom testRoom;
    private CinemaRoomDetail testDetail;

    @Before
    public void setUp() {
        CinemaRoom room = new CinemaRoom("Detail Test Room", 8);
        testDetail = new CinemaRoomDetail(
                200_000, LocalDate.of(2024, 3, 10), "Integration test room");
        room.setDetail(testDetail);
        testRoom = roomDao.saveRoom(room);
        // After cascade save, detail has its generated id
        testDetail = testRoom.getCinemaRoomDetail();
    }

    @After
    public void tearDown() {
        roomDao.deleteRoomById(testRoom.getCinemaRoomId()); // cascade removes detail too
    }

    @AfterClass
    public static void tearDownClass() {
        HibernateUtil.shutdown();
    }

    @Test
    public void findRoomDetailById_existingId_returnsDetail() {
        Optional<CinemaRoomDetail> result =
                detailDao.findRoomDetailById(testDetail.getCinemaRoomDetailId());

        assertTrue("Detail should be present", result.isPresent());
        assertEquals(200_000, result.get().getRoomRate());
        assertEquals(LocalDate.of(2024, 3, 10), result.get().getActiveDate());
        assertEquals("Integration test room", result.get().getRoomDescription());
    }

    @Test
    public void findRoomDetailById_nonExistingId_returnsEmpty() {
        Optional<CinemaRoomDetail> result =
                detailDao.findRoomDetailById(Integer.MAX_VALUE);

        assertFalse("Should be empty for unknown id", result.isPresent());
    }

    @Test
    public void findAllRoomDetails_atLeastOneRecord() {
        List<CinemaRoomDetail> details = detailDao.findAllRoomDetails();

        assertNotNull(details);
        assertTrue("At least 1 detail expected", details.size() >= 1);
    }

    @Test
    public void updateRoomDetailById_existingId_updatesFields() {
        LocalDate newDate = LocalDate.of(2025, 1, 1);
        boolean updated = detailDao.updateRoomDetailById(
                testDetail.getCinemaRoomDetailId(), 300_000, newDate, "Updated description");

        assertTrue("updateRoomDetailById should return true", updated);

        Optional<CinemaRoomDetail> reloaded =
                detailDao.findRoomDetailById(testDetail.getCinemaRoomDetailId());
        assertTrue(reloaded.isPresent());
        assertEquals(300_000, reloaded.get().getRoomRate());
        assertEquals(newDate, reloaded.get().getActiveDate());
        assertEquals("Updated description", reloaded.get().getRoomDescription());
    }

    @Test
    public void updateRoomDetailById_nonExistingId_returnsFalse() {
        assertFalse(detailDao.updateRoomDetailById(
                Integer.MAX_VALUE, 0, LocalDate.now(), "x"));
    }
}
