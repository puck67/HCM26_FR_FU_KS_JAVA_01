package fa.training.dao.impl;

import fa.training.dao.SeatDao;
import fa.training.entities.Seat;
import fa.training.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Collections;
import java.util.List;

public class SeatDaoImpl implements SeatDao {

    @Override
    public boolean insertSeat(Seat seat) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(seat);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Seat getSeatById(int id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(Seat.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Seat> getAllSeats() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Query<Seat> query = session.createQuery("FROM Seat", Seat.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public boolean updateSeatById(int id, Seat seat) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            Seat existingSeat = session.get(Seat.class, id);
            if (existingSeat != null) {
                existingSeat.setSeatColumn(seat.getSeatColumn());
                existingSeat.setSeatRow(seat.getSeatRow());
                existingSeat.setSeatStatus(seat.getSeatStatus());
                existingSeat.setSeatType(seat.getSeatType());
                session.update(existingSeat);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public boolean deleteSeatById(int id) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            Seat seat = session.get(Seat.class, id);
            if (seat != null) {
                // Decouple from CinemaRoom
                if (seat.getCinemaRoom() != null) {
                    seat.getCinemaRoom().getSeats().remove(seat);
                }
                session.delete(seat);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
