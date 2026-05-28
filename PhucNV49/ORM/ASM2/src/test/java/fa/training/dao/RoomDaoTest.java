package fa.training.dao;

import fa.training.dao.impl.RoomDaoImpl;
import fa.training.entities.CinemaRoom;
import fa.training.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RoomDaoTest {
    private final RoomDao roomDao = new RoomDaoImpl();

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
    }

    @Test
    public void testInsertRoom() {
        CinemaRoom room = new CinemaRoom("Room 01", 100);
        boolean success = roomDao.insertRoom(room);
        
        assertTrue(success);
        assertNotEquals(0, room.getCinemaRoomId());
    }

    @Test
    public void testGetRoomByID() {
        CinemaRoom room = new CinemaRoom("Room 02", 150);
        roomDao.insertRoom(room);

        CinemaRoom fetched = roomDao.getRoomByID(room.getCinemaRoomId());
        assertNotNull(fetched);
        assertEquals("Room 02", fetched.getCinemaRoomName());
        assertEquals(150, fetched.getSeatQuantity());
    }

    @Test
    public void testGetAllRoom() {
        CinemaRoom r1 = new CinemaRoom("Room A", 50);
        CinemaRoom r2 = new CinemaRoom("Room B", 60);
        roomDao.insertRoom(r1);
        roomDao.insertRoom(r2);

        List<CinemaRoom> rooms = roomDao.getAllRoom();
        assertEquals(2, rooms.size());
    }

    @Test
    public void testUpdateRoomByID() {
        CinemaRoom room = new CinemaRoom("Room C", 80);
        roomDao.insertRoom(room);

        room.setCinemaRoomName("Room C Updated");
        room.setSeatQuantity(90);
        boolean success = roomDao.updateRoomByID(room);
        
        assertTrue(success);
        
        CinemaRoom updated = roomDao.getRoomByID(room.getCinemaRoomId());
        assertEquals("Room C Updated", updated.getCinemaRoomName());
        assertEquals(90, updated.getSeatQuantity());
    }

    @Test
    public void testDeleteRoomById() {
        CinemaRoom room = new CinemaRoom("Room D", 120);
        roomDao.insertRoom(room);

        boolean success = roomDao.deleteRoomById(room.getCinemaRoomId());
        assertTrue(success);

        CinemaRoom fetched = roomDao.getRoomByID(room.getCinemaRoomId());
        assertNull(fetched);
    }
}
