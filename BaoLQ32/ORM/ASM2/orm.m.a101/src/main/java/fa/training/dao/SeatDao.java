package fa.training.dao;

import fa.training.entities.Seat;
import fa.training.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

/**
 * DAO for Seat entity.
 *
 * Extra query methods (findSeatsByRoomId, findSeatsByStatus) demonstrate
 * HQL usage with parameters — preferred over raw SQL in Hibernate.
 */
public class SeatDao {

    public Optional<Seat> findSeatById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Seat.class, id));
        }
    }

    public List<Seat> findAllSeats() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Seat> query = session.createQuery(
                    "FROM Seat ORDER BY seatId", Seat.class);
            return query.list();
        }
    }

    /** Extra: find all seats belonging to a specific room. */
    public List<Seat> findSeatsByRoomId(int roomId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Seat> query = session.createQuery(
                    "FROM Seat s WHERE s.cinemaRoom.cinemaRoomId = :roomId ORDER BY s.seatRow, s.seatColumn",
                    Seat.class);
            query.setParameter("roomId", roomId);
            return query.list();
        }
    }

    /** Extra: find all seats with a given status ('Available', 'Not Available', 'Booked'). */
    public List<Seat> findSeatsByStatus(String status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Seat> query = session.createQuery(
                    "FROM Seat s WHERE s.seatStatus = :status", Seat.class);
            query.setParameter("status", status);
            return query.list();
        }
    }

    public Seat saveSeat(Seat seat) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.save(seat);
            tx.commit();
            return seat;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Failed to save Seat", e);
        }
    }

    /**
     * Update status and type of a seat by id.
     *
     * @return true if found and updated, false if id does not exist
     */
    public boolean updateSeatById(int id, String newStatus, String newType) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Seat seat = session.get(Seat.class, id);
            if (seat == null) {
                tx.rollback();
                return false;
            }
            seat.setSeatStatus(newStatus);
            seat.setSeatType(newType);
            session.update(seat);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Failed to update Seat id=" + id, e);
        }
    }

    /**
     * @return true if found and deleted, false if id does not exist
     */
    public boolean deleteSeatById(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Seat seat = session.get(Seat.class, id);
            if (seat == null) {
                tx.rollback();
                return false;
            }
            session.delete(seat);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Failed to delete Seat id=" + id, e);
        }
    }
}
