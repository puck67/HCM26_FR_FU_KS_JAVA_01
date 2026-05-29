package fa.training.dao;

import fa.training.entities.CinemaRoom;
import fa.training.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class RoomDao {

    public CinemaRoom getRoomById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(CinemaRoom.class, id);
        }
    }

    public List<CinemaRoom> getAllRooms() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<CinemaRoom> query = session.createQuery("FROM CinemaRoom", CinemaRoom.class);
            return query.list();
        }
    }

    public CinemaRoom insertRoom(CinemaRoom room) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(room);
            tx.commit();
            return room;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public boolean updateRoomById(Long id, String newName, int newCapacity) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            CinemaRoom room = session.get(CinemaRoom.class, id);
            if (room == null) {
                tx.rollback();
                return false;
            }
            room.setRoomName(newName);
            room.setCapacity(newCapacity);
            session.update(room);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public boolean deleteRoomById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            CinemaRoom room = session.get(CinemaRoom.class, id);
            if (room == null) {
                tx.rollback();
                return false;
            }
            session.delete(room);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public List<CinemaRoom> getRoomsByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<CinemaRoom> query = session.createQuery(
                    "FROM CinemaRoom WHERE LOWER(roomName) LIKE LOWER(:name)", CinemaRoom.class);
            query.setParameter("name", "%" + name + "%");
            return query.list();
        }
    }
}
