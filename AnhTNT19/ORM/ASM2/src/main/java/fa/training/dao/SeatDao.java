package fa.training.dao;

import fa.training.entities.Seat;
import fa.training.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class SeatDao {

    public boolean insertSeat(Seat seat) {
        Transaction tx = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.save(seat);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public Seat getSeatById(int id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(Seat.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Seat> getAllSeats() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("FROM Seat", Seat.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean updateSeatById(int id, String newStatus, String newType) {
        Transaction tx = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Seat seat = session.get(Seat.class, id);
            if (seat == null) return false;
            seat.setSeatStatus(newStatus);
            seat.setSeatType(newType);
            session.update(seat);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteSeatById(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Seat seat = session.get(Seat.class, id);
            if (seat == null) return false;
            session.delete(seat);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public List<Seat> getSeatsByRoomId(int cinemaRoomId) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Seat s WHERE s.cinemaRoom.cinemaRoomId = :roomId", Seat.class)
                    .setParameter("roomId", cinemaRoomId)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Seat> getSeatsByStatus(String status) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Seat s WHERE s.seatStatus = :status", Seat.class)
                    .setParameter("status", status)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean bookSeat(int seatId) {
        Transaction tx = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Seat seat = session.get(Seat.class, seatId);
            if (seat == null) return false;
            if (!"Available".equals(seat.getSeatStatus())) return false;
            seat.setSeatStatus("Booked");
            session.update(seat);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }
}