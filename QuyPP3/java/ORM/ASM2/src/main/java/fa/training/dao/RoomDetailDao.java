package fa.training.dao;

import fa.training.config.HibernateUtils;
import fa.training.entities.CinemaRoomDetail;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class RoomDetailDao {

    public void insertRoomDetail(CinemaRoomDetail detail) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(detail);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public CinemaRoomDetail getRoomDetailById(int id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(CinemaRoomDetail.class, id);
        }
    }

    public List<CinemaRoomDetail> getAllRoomDetails() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from CinemaRoomDetail", CinemaRoomDetail.class).list();
        }
    }

    public void updateRoomDetail(CinemaRoomDetail detail) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(detail);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void deleteRoomDetailById(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            CinemaRoomDetail detail = session.get(CinemaRoomDetail.class, id);
            if (detail != null) {
                session.delete(detail);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
}
