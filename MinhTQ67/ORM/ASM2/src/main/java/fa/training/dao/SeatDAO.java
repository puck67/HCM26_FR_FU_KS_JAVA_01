package fa.training.dao;

import fa.training.entities.Seat;
import fa.training.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * DAO for Seat entity.
 * Provides 5 CRUD operations: insert, getByID, getAll, updateByID, deleteByID.
 */
public class SeatDAO {

    /**
     * Insert a new Seat (must have cinemaRoom set first).
     */
    public void insertSeat(Seat seat) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(seat);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    /**
     * Find a Seat by its ID.
     */
    public Seat getSeatByID(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Seat.class, id);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get all Seats.
     */
    public List<Seat> getAllSeats() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Seat", Seat.class).list();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get all Seats belonging to a specific CinemaRoom.
     */
    public List<Seat> getSeatsByRoomID(int roomId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Seat s WHERE s.cinemaRoom.cinemaRoomId = :roomId", Seat.class)
                    .setParameter("roomId", roomId)
                    .list();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Update status and type of a Seat by ID.
     */
    public void updateSeatByID(int id, String newStatus, String newType) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Seat seat = session.get(Seat.class, id);
            if (seat != null) {
                seat.setSeatStatus(newStatus);
                seat.setSeatType(newType);
                session.update(seat);
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    /**
     * Delete a Seat by ID.
     */
    public void deleteSeatByID(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Seat seat = session.get(Seat.class, id);
            if (seat != null) {
                session.delete(seat);
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }
}
