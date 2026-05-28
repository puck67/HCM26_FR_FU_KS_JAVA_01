package fa.training.dao;

import fa.training.entities.CinemaRoomDetail;
import fa.training.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.List;

/**
 * DAO for CinemaRoomDetail entity.
 * Provides 5 CRUD operations: insert, getByID, getAll, updateByID, deleteByID.
 */
public class RoomDetailDAO {

    /**
     * Insert a new CinemaRoomDetail (must have cinemaRoom set first).
     */
    public void insertRoomDetail(CinemaRoomDetail detail) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(detail);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    /**
     * Find a CinemaRoomDetail by its ID.
     */
    public CinemaRoomDetail getRoomDetailByID(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(CinemaRoomDetail.class, id);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get all CinemaRoomDetails.
     */
    public List<CinemaRoomDetail> getAllRoomDetails() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM CinemaRoomDetail", CinemaRoomDetail.class).list();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Update roomRate, activeDate, and description of a CinemaRoomDetail by ID.
     */
    public void updateRoomDetailByID(int id, int newRate, LocalDate newDate, String newDescription) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            CinemaRoomDetail detail = session.get(CinemaRoomDetail.class, id);
            if (detail != null) {
                detail.setRoomRate(newRate);
                detail.setActiveDate(newDate);
                detail.setRoomDescription(newDescription);
                session.update(detail);
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    /**
     * Delete a CinemaRoomDetail by ID.
     */
    public void deleteRoomDetailByID(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            CinemaRoomDetail detail = session.get(CinemaRoomDetail.class, id);
            if (detail != null) {
                session.delete(detail);
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }
}
