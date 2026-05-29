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
        if (seat == null) {
            System.err.println(new StringBuilder().append("Error: Seat is null."));
            return false;
        }
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(seat);
            transaction.commit();
            return true;
        } catch (Exception e) {
            try {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
            } catch (Exception rollbackEx) {
            }
            System.err.println(new StringBuilder()
                    .append("Error inserting seat: ")
                    .append(e.getMessage()));
            return false;
        }
    }

    @Override
    public Seat getSeatByID(int id) {
        if (id <= 0) {
            System.err.println(new StringBuilder().append("Error: Invalid ID."));
            return null;
        }
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(Seat.class, id);
        } catch (Exception e) {
            System.err.println(new StringBuilder()
                    .append("Error getting seat by ID: ")
                    .append(e.getMessage()));
            return null;
        }
    }

    @Override
    public List<Seat> getAllSeat() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            StringBuilder hql = new StringBuilder("from Seat");
            Query<Seat> query = session.createQuery(hql.toString(), Seat.class);
            return query.list();
        } catch (Exception e) {
            System.err.println(new StringBuilder()
                    .append("Error getting all seats: ")
                    .append(e.getMessage()));
            return Collections.emptyList();
        }
    }

    @Override
    public boolean updateSeatByID(int id, Seat seat) {
        if (id <= 0 || seat == null) {
            System.err.println(new StringBuilder().append("Error: Invalid update arguments."));
            return false;
        }
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Seat existing = session.get(Seat.class, id);
            if (existing != null) {
                existing.setSeatColumn(seat.getSeatColumn());
                existing.setSeatRow(seat.getSeatRow());
                existing.setSeatStatus(seat.getSeatStatus());
                existing.setSeatType(seat.getSeatType());
                if (seat.getCinemaRoom() != null) {
                    existing.setCinemaRoom(seat.getCinemaRoom());
                }
                session.merge(existing);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            try {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
            } catch (Exception rollbackEx) {
            }
            System.err.println(new StringBuilder()
                    .append("Error updating seat by ID: ")
                    .append(e.getMessage()));
            return false;
        }
    }

    @Override
    public boolean deleteSeatById(int id) {
        if (id <= 0) {
            System.err.println(new StringBuilder().append("Error: Invalid ID."));
            return false;
        }
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Seat seat = session.get(Seat.class, id);
            if (seat != null) {
                if (seat.getCinemaRoom() != null) {
                    seat.getCinemaRoom().getSeats().remove(seat);
                    session.merge(seat.getCinemaRoom());
                    seat.setCinemaRoom(null);
                }
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
            } catch (Exception rollbackEx) {
            }
            System.err.println(new StringBuilder()
                    .append("Error deleting seat by ID: ")
                    .append(e.getMessage()));
            return false;
        }
    }
}
