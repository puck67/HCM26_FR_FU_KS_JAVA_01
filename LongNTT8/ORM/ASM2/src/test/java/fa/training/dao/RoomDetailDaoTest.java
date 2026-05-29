package fa.training.dao;

import fa.training.entities.CinemaRoom;
import fa.training.entities.CinemaRoomDetail;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

public class RoomDetailDaoTest {

    private RoomDao roomDao;
    private RoomDetailDao detailDao;
    private CinemaRoom testRoom;
    private CinemaRoomDetail testDetail;

    @BeforeEach
    public void setUp() {
        roomDao = new RoomDao();
        detailDao = new RoomDetailDao();
        
        testRoom = new CinemaRoom("Room for Detail", 200);
        roomDao.insert(testRoom);
        
        testDetail = new CinemaRoomDetail(testRoom, 50000, LocalDate.now(), "Standard Room");
        detailDao.insert(testDetail);
    }

    @AfterEach
    public void tearDown() {
        if (testDetail != null && testDetail.getCinemaRoomDetailId() != null) {
            detailDao.deleteById(testDetail.getCinemaRoomDetailId());
        }
        if (testRoom != null && testRoom.getCinemaRoomId() != null) {
            roomDao.deleteById(testRoom.getCinemaRoomId());
        }
    }

    @Test
    public void testInsert() {
        CinemaRoom room = new CinemaRoom("Another Room", 300);
        roomDao.insert(room);
        
        CinemaRoomDetail detail = new CinemaRoomDetail(room, 60000, LocalDate.now(), "VIP Room");
        boolean result = detailDao.insert(detail);
        assertTrue(result, "Insert should return true");
        assertNotNull(detail.getCinemaRoomDetailId(), "Detail ID should be generated");

        detailDao.deleteById(detail.getCinemaRoomDetailId());
        roomDao.deleteById(room.getCinemaRoomId());
    }

    @Test
    public void testGetById() {
        CinemaRoomDetail retrieved = detailDao.getById(testDetail.getCinemaRoomDetailId());
        assertNotNull(retrieved, "Should retrieve detail");
        assertEquals("Standard Room", retrieved.getRoomDescription(), "Description should match");
    }

    @Test
    public void testUpdateById() {
        testDetail.setRoomDescription("Updated Description");
        testDetail.setRoomRate(70000);
        boolean updateResult = detailDao.updateById(testDetail);
        assertTrue(updateResult, "Update should return true");
        
        CinemaRoomDetail retrieved = detailDao.getById(testDetail.getCinemaRoomDetailId());
        assertEquals("Updated Description", retrieved.getRoomDescription(), "Description should be updated");
        assertEquals(70000, retrieved.getRoomRate(), "Rate should be updated");
    }

    @Test
    public void testDeleteById() {
        CinemaRoom room = new CinemaRoom("Temp Room", 50);
        roomDao.insert(room);
        CinemaRoomDetail detail = new CinemaRoomDetail(room, 10000, LocalDate.now(), "Temp");
        detailDao.insert(detail);
        
        Integer id = detail.getCinemaRoomDetailId();
        boolean deleteResult = detailDao.deleteById(id);
        assertTrue(deleteResult, "Delete should return true");
        
        assertNull(detailDao.getById(id), "Should not find deleted entity");
        roomDao.deleteById(room.getCinemaRoomId());
    }

    @Test
    public void testGetAll() {
        List<CinemaRoomDetail> details = detailDao.getAll();
        assertNotNull(details, "List should not be null");
        assertFalse(details.isEmpty(), "List should not be empty");
    }
}
