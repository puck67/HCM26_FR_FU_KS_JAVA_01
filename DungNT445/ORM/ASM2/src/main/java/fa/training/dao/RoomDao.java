package fa.training.dao;

import fa.training.config.HibernateUtils;
import fa.training.entities.CinemaRoom;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class RoomDao {

    public void insertRoom(CinemaRoom room) {
        Transaction tx = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.save(room);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public CinemaRoom getRoomById(int id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(CinemaRoom.class, id);
        }
    }

    public List<CinemaRoom> getAllRoom() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from CinemaRoom", CinemaRoom.class).list();
        }
    }

    public void updateRoomById(CinemaRoom room) {
        Transaction tx = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.update(room);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public void deleteRoomById(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            CinemaRoom room = session.get(CinemaRoom.class, id);
            if (room != null) {
                session.delete(room);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }
}
