package fa.training.dao;

import fa.training.entities.CinemaRoomDetail;
import fa.training.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * DAO for CinemaRoomDetail entity.
 *
 * Uses LocalDate for date fields (assignment guideline for high accuracy).
 * All Sessions managed with try-with-resources — no resource leaks.
 */
public class RoomDetailDao {

    public Optional<CinemaRoomDetail> findRoomDetailById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(CinemaRoomDetail.class, id));
        }
    }

    public List<CinemaRoomDetail> findAllRoomDetails() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<CinemaRoomDetail> query = session.createQuery(
                    "FROM CinemaRoomDetail ORDER BY cinemaRoomDetailId", CinemaRoomDetail.class);
            return query.list();
        }
    }

    public CinemaRoomDetail saveRoomDetail(CinemaRoomDetail detail) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.save(detail);
            tx.commit();
            return detail;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Failed to save CinemaRoomDetail", e);
        }
    }

    /**
     * Update rate, activeDate and description for a detail record.
     *
     * @return true if found and updated, false if id does not exist
     */
    public boolean updateRoomDetailById(int id, int newRate,
                                        LocalDate newActiveDate, String newDescription) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            CinemaRoomDetail detail = session.get(CinemaRoomDetail.class, id);
            if (detail == null) {
                tx.rollback();
                return false;
            }
            detail.setRoomRate(newRate);
            detail.setActiveDate(newActiveDate);
            detail.setRoomDescription(newDescription);
            session.update(detail);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Failed to update CinemaRoomDetail id=" + id, e);
        }
    }

    /**
     * @return true if found and deleted, false if id does not exist
     */
    public boolean deleteRoomDetailById(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
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
            throw new RuntimeException("Failed to delete CinemaRoomDetail id=" + id, e);
        }
    }
}
