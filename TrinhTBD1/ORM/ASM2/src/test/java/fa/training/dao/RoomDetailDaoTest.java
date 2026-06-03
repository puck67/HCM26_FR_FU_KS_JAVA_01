package fa.training.dao;

import fa.training.dao.impl.RoomDaoImpl;
import fa.training.dao.impl.RoomDetailDaoImpl;
import fa.training.entities.CinemaRoom;
import fa.training.entities.CinemaRoomDetail;
import fa.training.utils.HibernateUtils;
import org.junit.jupiter.api.*;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class RoomDetailDaoTest {

        private static RoomDao roomDao;
        private static RoomDetailDao roomDetailDao;
        private CinemaRoom testRoom;

        @BeforeAll
        public static void setUpClass() {
                roomDao = new RoomDaoImpl();
                roomDetailDao = new RoomDetailDaoImpl();
        }

        @AfterAll
        public static void tearDownClass() {
                HibernateUtils.shutdown();
        }

        @BeforeEach
        public void cleanDatabase() {
                List<CinemaRoom> rooms = roomDao.getAllRoom();
                for (CinemaRoom r : rooms) {
                        roomDao.deleteRoomById(r.getCinemaRoomId());
                }

                testRoom = new CinemaRoom("Test Cinema Room B", 60);
                roomDao.insertRoom(testRoom);
        }

        @Test
        public void testInsertRoomDetail_Success() {
                CinemaRoomDetail detail = new CinemaRoomDetail(
                                120000,
                                LocalDate.now(),
                                "Standard IMAX Room Detail Specifications");
                detail.setCinemaRoom(testRoom);

                boolean result = roomDetailDao.insertRoomDetail(detail);

                assertTrue(result, new StringBuilder()
                                .append("Failed to insert cinema room detail. Rate: ")
                                .append(detail.getRoomRate())
                                .toString());
                assertNotNull(detail.getCinemaRoomDetailId());
        }

        @Test
        public void testInsertRoomDetail_DuplicateRoom_ShouldFail() {
                CinemaRoomDetail detail1 = new CinemaRoomDetail(
                                100000,
                                LocalDate.now(),
                                "First Specification File");
                detail1.setCinemaRoom(testRoom);
                boolean result1 = roomDetailDao.insertRoomDetail(detail1);
                assertTrue(result1);

                CinemaRoomDetail detail2 = new CinemaRoomDetail(
                                150000,
                                LocalDate.now(),
                                "Second Specification File trying to bind to same Room");
                detail2.setCinemaRoom(testRoom);

                boolean result2 = roomDetailDao.insertRoomDetail(detail2);
                assertFalse(result2, new StringBuilder()
                                .append("Should fail when attempting to insert duplicate room detail for the same room (One-to-One constraint).")
                                .toString());
        }

        @Test
        public void testGetRoomDetailById() {
                CinemaRoomDetail detail = new CinemaRoomDetail(
                                130000,
                                LocalDate.now(),
                                "Dolby Atmos Surround System Specs");
                detail.setCinemaRoom(testRoom);
                roomDetailDao.insertRoomDetail(detail);
                Integer id = detail.getCinemaRoomDetailId();

                CinemaRoomDetail fetched = roomDetailDao.getRoomDetailByID(id);
                assertNotNull(fetched);
                assertEquals(130000, fetched.getRoomRate());
                assertEquals("Dolby Atmos Surround System Specs", fetched.getRoomDescription());
                assertEquals(LocalDate.now(), fetched.getActiveDate());
        }

        @Test
        public void testUpdateRoomDetail_Success() {
                CinemaRoomDetail detail = new CinemaRoomDetail(
                                110000,
                                LocalDate.now(),
                                "Old Room Details");
                detail.setCinemaRoom(testRoom);
                roomDetailDao.insertRoomDetail(detail);
                Integer id = detail.getCinemaRoomDetailId();

                CinemaRoomDetail updateData = new CinemaRoomDetail(
                                180000,
                                LocalDate.now(),
                                "Old Room Details");
                boolean result = roomDetailDao.updateRoomDetailByID(id, updateData);
                assertTrue(result);

                CinemaRoomDetail fetched = roomDetailDao.getRoomDetailByID(id);
                assertEquals(180000, fetched.getRoomRate(), new StringBuilder()
                                .append("Expected room rate updated to 180000, but got ")
                                .append(fetched.getRoomRate())
                                .toString());
        }

        @Test
        public void testDeleteRoomDetail_Success() {
                CinemaRoomDetail detail = new CinemaRoomDetail(
                                140000,
                                LocalDate.now(),
                                "Delete specifications");
                detail.setCinemaRoom(testRoom);
                roomDetailDao.insertRoomDetail(detail);
                Integer id = detail.getCinemaRoomDetailId();

                boolean result = roomDetailDao.deleteRoomDetailById(id);
                assertTrue(result);

                CinemaRoomDetail fetchedDetail = roomDetailDao.getRoomDetailByID(id);
                assertNull(fetchedDetail, new StringBuilder()
                                .append("Deleted room detail should not exist in the database.")
                                .toString());

                CinemaRoom parentRoom = roomDao.getRoomByID(testRoom.getCinemaRoomId());
                assertNotNull(parentRoom, new StringBuilder()
                                .append("Parent CinemaRoom should remain in database after detail deletion.")
                                .toString());
        }

        @Test
        public void testInsertRoomDetail_Null_ShouldFail() {
                boolean result = roomDetailDao.insertRoomDetail(null);
                assertFalse(result, new StringBuilder()
                                .append("Inserting null room detail must return false, not crash.")
                                .toString());
        }

        @Test
        public void testGetRoomDetailById_InvalidId_ShouldReturnNull() {
                CinemaRoomDetail fetched = roomDetailDao.getRoomDetailByID(-3);
                assertNull(fetched, new StringBuilder()
                                .append("Invalid negative ID must return null, not crash.")
                                .toString());
        }

        @Test
        public void testUpdateRoomDetail_Null_ShouldFail() {
                boolean result = roomDetailDao.updateRoomDetailByID(1, null);
                assertFalse(result, new StringBuilder()
                                .append("Updating with null detail data must return false, not crash.")
                                .toString());
        }

        @Test
        public void testUpdateRoomDetail_InvalidId_ShouldFail() {
                CinemaRoomDetail detail = new CinemaRoomDetail(10000, LocalDate.now(), "Valid");
                boolean result = roomDetailDao.updateRoomDetailByID(-1, detail);
                assertFalse(result, new StringBuilder()
                                .append("Updating with negative ID must return false, not crash.")
                                .toString());
        }

        @Test
        public void testDeleteRoomDetailById_InvalidId_ShouldFail() {
                boolean result = roomDetailDao.deleteRoomDetailById(-10);
                assertFalse(result, new StringBuilder()
                                .append("Deleting with negative ID must return false, not crash.")
                                .toString());
        }
}
