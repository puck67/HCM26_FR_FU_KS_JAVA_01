package fa.training.dao.impl;

import fa.training.dao.SeatDao;
import fa.training.entities.Seat;
import fa.training.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.Collections;
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
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Seat getSeatById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Seat.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Seat> getAllSeat() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Seat", Seat.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public boolean updateSeatById(Seat seat) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(seat);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
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
                if (seat.getCinemaRoom() != null) {
                    seat.getCinemaRoom().getSeats().remove(seat);
                    seat.setCinemaRoom(null);
                }
                session.remove(seat);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }
}
