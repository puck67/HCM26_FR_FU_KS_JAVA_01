package fa.training.dao;

import fa.training.entities.Seat;
import fa.training.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class SeatDao {

    public Seat getSeatById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Seat.class, id);
        }
    }

    public List<Seat> getAllSeats() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Seat> query = session.createQuery("FROM Seat", Seat.class);
            return query.list();
        }
    }

    public Seat insertSeat(Seat seat) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(seat);
            tx.commit();
            return seat;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public boolean updateSeatById(Long id, String newStatus, String newType) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
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
            throw e;
        } finally {
            session.close();
        }
    }

    public boolean deleteSeatById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
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
            throw e;
        } finally {
            session.close();
        }
    }

    public List<Seat> getSeatsByRoomAndStatus(Long roomId, String status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Seat> query = session.createQuery(
                    "FROM Seat WHERE cinemaRoom.roomId = :roomId AND seatStatus = :status", Seat.class);
            query.setParameter("roomId", roomId);
            query.setParameter("status", status);
            return query.list();
        }
    }

    public List<Seat> getSeatsByType(String seatType) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Seat> query = session.createQuery(
                    "FROM Seat WHERE seatType = :type", Seat.class);
            query.setParameter("type", seatType);
            return query.list();
        }
    }
}
