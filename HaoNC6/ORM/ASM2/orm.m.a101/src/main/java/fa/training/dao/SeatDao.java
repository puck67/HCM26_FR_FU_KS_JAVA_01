package fa.training.dao;

import fa.training.entities.Seat;
import fa.training.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class SeatDao {

    // 1. Insert
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

    // 2. Get By ID
    public Seat getSeatById(int id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(Seat.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 3. Get All
    public List<Seat> getAllSeat() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Seat", Seat.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 4. Update By ID
    public void updateSeatById(Seat updatedSeat) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(updatedSeat);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // 5. Delete By ID
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