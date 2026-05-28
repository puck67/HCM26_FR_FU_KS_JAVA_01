package fa.training.dao;

import fa.training.config.HibernateUtils;
import fa.training.entities.CinemaRoom;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class RoomDao {

    public void insertRoom(CinemaRoom room) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(room);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public CinemaRoom getRoomById(int id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery(
                "select distinct r from CinemaRoom r " +
                "left join fetch r.cinemaRoomDetail " +
                "left join fetch r.seats " +
                "where r.cinemaRoomId = :id", CinemaRoom.class)
                .setParameter("id", id)
                .uniqueResult();
        }
    }

    public List<CinemaRoom> getAllRooms() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery(
                "select distinct r from CinemaRoom r " +
                "left join fetch r.cinemaRoomDetail " +
                "left join fetch r.seats", CinemaRoom.class).list();
        }
    }

    public void updateRoom(CinemaRoom room) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(room);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void deleteRoomById(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            CinemaRoom room = session.get(CinemaRoom.class, id);
            if (room != null) {
                session.delete(room);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
}
