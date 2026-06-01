package fa.training.daos;

import fa.training.entities.CinemaRoom;
import fa.training.entities.CinemaRoomDetail;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

/**
 * RoomDetailDaoTest - Unit tests for RoomDetailDao
 */
public class RoomDetailDaoTest {
    private static RoomDetailDao detailDao;
    private static RoomDao roomDao;

    public RoomDetailDaoTest() {
        detailDao = new RoomDetailDao();
        roomDao = new RoomDao();
    }

    @Test
    public void testInsertRoomDetail() {
        CinemaRoom room = new CinemaRoom("Room for Detail Test 1", 50);
        CinemaRoomDetail detail = new CinemaRoomDetail(150, LocalDate.now(), "Test Detail");
        room.setRoomDetail(detail);

        Integer roomId = roomDao.insertRoom(room);
        assertNotNull(roomId);

        CinemaRoom roomFetched = roomDao.getRoomById(roomId);
        assertNotNull(roomFetched.getRoomDetail());
        assertNotNull(roomFetched.getRoomDetail().getCinemaRoomDetailId());
        System.out.println("✓ testInsertRoomDetail passed with ID: " + roomFetched.getRoomDetail().getCinemaRoomDetailId());
    }

    @Test
    public void testGetRoomDetailById() {
        CinemaRoom room = new CinemaRoom("Room for Detail Test 2", 60);
        CinemaRoomDetail detail = new CinemaRoomDetail(200, LocalDate.now(), "Detail Test");
        room.setRoomDetail(detail);
        roomDao.insertRoom(room);

        CinemaRoomDetail retrievedDetail = detailDao.getRoomDetailById(detail.getCinemaRoomDetailId());
        assertNotNull(retrievedDetail);
        assertEquals(Integer.valueOf(200), retrievedDetail.getRoomRate());
        assertEquals("Detail Test", retrievedDetail.getRoomDescription());
        System.out.println("✓ testGetRoomDetailById passed");
    }

    @Test
    public void testGetAllRoomDetails() {
        for (int i = 1; i <= 3; i++) {
            CinemaRoom room = new CinemaRoom("Room for Detail Test Multiple " + i, 45);
            CinemaRoomDetail detail = new CinemaRoomDetail(150 + i * 50, LocalDate.now(), "Detail " + i);
            room.setRoomDetail(detail);
            roomDao.insertRoom(room);
        }

        List<CinemaRoomDetail> allDetails = detailDao.getAllRoomDetails();
        assertNotNull(allDetails);
        assertTrue(allDetails.size() >= 3);
        System.out.println("✓ testGetAllRoomDetails passed with " + allDetails.size() + " details");
    }

    @Test
    public void testGetRoomDetailsByRateRange() {
        CinemaRoom room1 = new CinemaRoom("Room Rate Range 1", 40);
        CinemaRoomDetail detail1 = new CinemaRoomDetail(100, LocalDate.now(), "Low Rate");
        room1.setRoomDetail(detail1);
        roomDao.insertRoom(room1);

        CinemaRoom room2 = new CinemaRoom("Room Rate Range 2", 40);
        CinemaRoomDetail detail2 = new CinemaRoomDetail(250, LocalDate.now(), "Mid Rate");
        room2.setRoomDetail(detail2);
        roomDao.insertRoom(room2);

        CinemaRoom room3 = new CinemaRoom("Room Rate Range 3", 40);
        CinemaRoomDetail detail3 = new CinemaRoomDetail(500, LocalDate.now(), "High Rate");
        room3.setRoomDetail(detail3);
        roomDao.insertRoom(room3);

        List<CinemaRoomDetail> midRangeDetails = detailDao.getRoomDetailsByRateRange(150, 300);
        assertNotNull(midRangeDetails);
        assertTrue(midRangeDetails.size() >= 1);
        System.out.println("✓ testGetRoomDetailsByRateRange passed");
    }

    @Test
    public void testGetRoomDetailsActiveAfter() {
        LocalDate pastDate = LocalDate.now().minusDays(30);
        LocalDate futureDate = LocalDate.now().plusDays(30);

        CinemaRoom room1 = new CinemaRoom("Room Active Date 1", 30);
        CinemaRoomDetail detail1 = new CinemaRoomDetail(150, pastDate, "Past Date");
        room1.setRoomDetail(detail1);
        roomDao.insertRoom(room1);

        CinemaRoom room2 = new CinemaRoom("Room Active Date 2", 30);
        CinemaRoomDetail detail2 = new CinemaRoomDetail(200, futureDate, "Future Date");
        room2.setRoomDetail(detail2);
        roomDao.insertRoom(room2);

        List<CinemaRoomDetail> futureDetails = detailDao.getRoomDetailsActiveAfter(LocalDate.now());
        assertNotNull(futureDetails);
        System.out.println("✓ testGetRoomDetailsActiveAfter passed");
    }

    @Test
    public void testUpdateRoomDetailById() {
        CinemaRoom room = new CinemaRoom("Room for Update Test", 70);
        CinemaRoomDetail detail = new CinemaRoomDetail(150, LocalDate.now(), "Old Description");
        room.setRoomDetail(detail);
        roomDao.insertRoom(room);

        CinemaRoomDetail updatedDetail = new CinemaRoomDetail(250, LocalDate.now().plusDays(10), "New Description");
        updatedDetail.setCinemaRoom(room);
        detailDao.updateRoomDetailById(detail.getCinemaRoomDetailId(), updatedDetail);

        CinemaRoomDetail resultDetail = detailDao.getRoomDetailById(detail.getCinemaRoomDetailId());
        assertNotNull(resultDetail);
        assertEquals(Integer.valueOf(250), resultDetail.getRoomRate());
        assertEquals("New Description", resultDetail.getRoomDescription());
        System.out.println("✓ testUpdateRoomDetailById passed");
    }

    @Test
    public void testDeleteRoomDetailById() {
        CinemaRoom room = new CinemaRoom("Room for Delete Test", 80);
        CinemaRoomDetail detail = new CinemaRoomDetail(150, LocalDate.now(), "To Be Deleted");
        room.setRoomDetail(detail);
        roomDao.insertRoom(room);

        Integer detailId = detail.getCinemaRoomDetailId();
        roomDao.deleteRoomById(room.getCinemaRoomId());

        CinemaRoomDetail deletedDetail = detailDao.getRoomDetailById(detailId);
        assertNull(deletedDetail);
        System.out.println("✓ testDeleteRoomDetailById passed");
    }

    @Test
    public void testCountAllRoomDetails() {
        Long count = detailDao.countAllRoomDetails();
        assertNotNull(count);
        assertTrue(count >= 0);
        System.out.println("✓ testCountAllRoomDetails passed with count: " + count);
    }
}
