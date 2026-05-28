package fa.training.dao;

import fa.training.config.HibernateUtils;
import fa.training.entities.CinemaRoomDetail;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class RoomDetailDao {

    public void insertRoomDetail(CinemaRoomDetail detail) {
        Transaction tx = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.save(detail);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public CinemaRoomDetail getRoomDetailById(int id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(CinemaRoomDetail.class, id);
        }
    }

    public List<CinemaRoomDetail> getAllRoomDetail() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from CinemaRoomDetail", CinemaRoomDetail.class).list();
        }
    }

    public void updateRoomDetailById(CinemaRoomDetail detail) {
        Transaction tx = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.update(detail);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public void deleteRoomDetailById(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            CinemaRoomDetail detail = session.get(CinemaRoomDetail.class, id);
            if (detail != null) {
                session.delete(detail);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }
}
