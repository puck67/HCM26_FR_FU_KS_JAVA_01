package fa.training.dao.impl;

import fa.training.dao.SeatDao;
import fa.training.entities.Seat;
import fa.training.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.ArrayList;
import java.util.List;

public class SeatDaoImpl implements SeatDao {
    @Override
    public boolean insertSeat(Seat seat) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(seat);
            transaction.commit();
            return true;
        } catch (Exception e) {
            try {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
            } catch (Exception ex) {
                System.err.println("Rollback failed: " + ex.getMessage());
            }
            System.err.println("Error saving seat: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Seat getSeatByID(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Seat.class, id);
        } catch (Exception e) {
            System.err.println("Error retrieving seat by ID: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Seat> getAllSeat() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Seat", Seat.class).list();
        } catch (Exception e) {
            System.err.println("Error listing seats: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public boolean updateSeatByID(Seat seat) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(seat);
            transaction.commit();
            return true;
        } catch (Exception e) {
            try {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
            } catch (Exception ex) {
                System.err.println("Rollback failed: " + ex.getMessage());
            }
            System.err.println("Error updating seat: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteSeatById(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Seat seat = session.get(Seat.class, id);
            if (seat != null) {
                session.remove(seat);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            try {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
            } catch (Exception ex) {
                System.err.println("Rollback failed: " + ex.getMessage());
            }
            System.err.println("Error deleting seat: " + e.getMessage());
            return false;
        }
    }
}
