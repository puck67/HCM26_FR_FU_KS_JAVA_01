package fa.training.dao;

import fa.training.config.HibernateUtils;
import fa.training.entities.Seat;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class SeatDao {

    public void insertSeat(Seat seat) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(seat);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public Seat getSeatById(int id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery(
                "select s from Seat s " +
                "left join fetch s.cinemaRoom " +
                "where s.seatId = :id", Seat.class)
                .setParameter("id", id)
                .uniqueResult();
        }
    }

    public List<Seat> getAllSeats() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Seat", Seat.class).list();
        }
    }

    public void updateSeat(Seat seat) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(seat);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void deleteSeatById(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Seat seat = session.get(Seat.class, id);
            if (seat != null) {
                session.delete(seat);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
}
