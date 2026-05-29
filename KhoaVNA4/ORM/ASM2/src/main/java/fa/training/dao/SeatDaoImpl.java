package fa.training.dao;

import fa.training.entities.Seat;
import fa.training.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import jakarta.persistence.PersistenceException;
import java.util.Collections;
import java.util.List;

public class SeatDaoImpl implements SeatDao {

    @Override
    public boolean insertSeat(Seat seat) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(seat);
            transaction.commit();
            return true;
        } catch (PersistenceException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Insert Seat failed (Database/Mapping error): " + e.getMessage());
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Insert Seat failed (System error): " + e.getMessage());
            return false;
        }
    }

    @Override
    public Seat getSeatById(int id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("SELECT s FROM Seat s LEFT JOIN FETCH s.cinemaRoom WHERE s.seatId = :id", Seat.class)
                    .setParameter("id", id)
                    .uniqueResult();
        } catch (PersistenceException e) {
            System.err.println("Get Seat by ID failed (Database error): " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Get Seat by ID failed: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Seat> getAllSeat() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("SELECT s FROM Seat s LEFT JOIN FETCH s.cinemaRoom", Seat.class).list();
        } catch (PersistenceException e) {
            System.err.println("Get All Seats failed (Database error): " + e.getMessage());
            return Collections.emptyList();
        } catch (Exception e) {
            System.err.println("Get All Seats failed: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public boolean updateSeatById(Seat seat) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Seat existing = session.get(Seat.class, seat.getSeatId());
            if (existing != null) {
                session.merge(seat);
                transaction.commit();
                return true;
            }
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        } catch (PersistenceException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Update Seat failed (Database/Mapping error): " + e.getMessage());
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Update Seat failed: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteSeatById(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Seat existing = session.get(Seat.class, id);
            if (existing != null) {
                session.remove(existing);
                transaction.commit();
                return true;
            }
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        } catch (PersistenceException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Delete Seat failed (Database error): " + e.getMessage());
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Delete Seat failed: " + e.getMessage());
            return false;
        }
    }
}
