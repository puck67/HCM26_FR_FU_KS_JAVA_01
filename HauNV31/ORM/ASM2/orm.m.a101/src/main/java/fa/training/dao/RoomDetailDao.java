package fa.training.dao;

import fa.training.entities.CinemaRoomDetail;
import fa.training.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;

public class RoomDetailDao {

    public CinemaRoomDetail getRoomDetailById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(CinemaRoomDetail.class, id);
        }
    }

    public List<CinemaRoomDetail> getAllRoomDetails() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<CinemaRoomDetail> query = session.createQuery("FROM CinemaRoomDetail", CinemaRoomDetail.class);
            return query.list();
        }
    }

    public CinemaRoomDetail insertRoomDetail(CinemaRoomDetail detail) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(detail);
            tx.commit();
            return detail;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public boolean updateRoomDetailById(Long id, double newRate, LocalDate newActiveDate, String newDescription) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            CinemaRoomDetail detail = session.get(CinemaRoomDetail.class, id);
            if (detail == null) {
                tx.rollback();
                return false;
            }
            detail.setRoomRate(newRate);
            detail.setActiveDate(newActiveDate);
            detail.setDescription(newDescription);
            session.update(detail);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public boolean deleteRoomDetailById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            CinemaRoomDetail detail = session.get(CinemaRoomDetail.class, id);
            if (detail == null) {
                tx.rollback();
                return false;
            }
            session.delete(detail);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public List<CinemaRoomDetail> getRoomDetailsByDateRange(LocalDate from, LocalDate to) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<CinemaRoomDetail> query = session.createQuery(
                    "FROM CinemaRoomDetail WHERE activeDate BETWEEN :from AND :to", CinemaRoomDetail.class);
            query.setParameter("from", from);
            query.setParameter("to", to);
            return query.list();
        }
    }
}
