package fa.training.dao;

import fa.training.config.HibernateUtils;
import fa.training.entities.Seat;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class SeatDao {

    public void insertSeat(Seat seat) {
        Transaction tx = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.save(seat);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public Seat getSeatById(int id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(Seat.class, id);
        }
    }

    public List<Seat> getAllSeat() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Seat", Seat.class).list();
        }
    }

    public void updateSeatById(Seat seat) {
        Transaction tx = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.update(seat);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public void deleteSeatById(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Seat seat = session.get(Seat.class, id);
            if (seat != null) {
                session.delete(seat);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }
}
