package fa.training.daos;

import fa.training.entities.Seat;
import fa.training.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Objects;

/**
 * SeatDao - Data Access Object for Seat entity
 */
public class SeatDao {

    /**
     * Insert a new seat
     */
    public Integer insertSeat(Seat seat) {
        Objects.requireNonNull(seat, "seat must not be null");
        Objects.requireNonNull(seat.getCinemaRoom(), "seat.cinemaRoom must not be null");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        Integer seatId = null;
        try {
            transaction = session.beginTransaction();
            seatId = (Integer) session.save(seat);
            transaction.commit();
            System.out.println("Seat inserted successfully with ID: " + seatId);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error inserting seat: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
        return seatId;
    }

    /**
     * Get seat by ID
     */
    public Seat getSeatById(Integer seatId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Seat seat = null;
        try {
            seat = session.get(Seat.class, seatId);
        } catch (Exception e) {
            System.err.println("Error retrieving seat: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
        return seat;
    }

    /**
     * Get all seats
     */
    public List<Seat> getAllSeats() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Seat> seats = null;
        try {
            Query<Seat> query = session.createQuery("FROM Seat", Seat.class);
            seats = query.list();
        } catch (Exception e) {
            System.err.println("Error retrieving all seats: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
        return seats;
    }

    /**
     * Get all seats by room ID
     */
    public List<Seat> getSeatsByRoomId(Integer roomId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Seat> seats = null;
        try {
            Query<Seat> query = session.createQuery("FROM Seat WHERE cinemaRoom.cinemaRoomId = :roomId", Seat.class);
            query.setParameter("roomId", roomId);
            seats = query.list();
        } catch (Exception e) {
            System.err.println("Error retrieving seats by room ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
        return seats;
    }

    /**
     * Get seats by status
     */
    public List<Seat> getSeatsByStatus(String status) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Seat> seats = null;
        try {
            Query<Seat> query = session.createQuery("FROM Seat WHERE seatStatus = :status", Seat.class);
            query.setParameter("status", status);
            seats = query.list();
        } catch (Exception e) {
            System.err.println("Error retrieving seats by status: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
        return seats;
    }

    /**
     * Update seat
     */
    public void updateSeatById(Integer seatId, Seat seatDetails) {
        Objects.requireNonNull(seatId, "seatId must not be null");
        Objects.requireNonNull(seatDetails, "seatDetails must not be null");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Seat seat = session.get(Seat.class, seatId);
            if (seat != null) {
                seat.setSeatRow(seatDetails.getSeatRow());
                seat.setSeatColumn(seatDetails.getSeatColumn());
                seat.setSeatStatus(seatDetails.getSeatStatus());
                seat.setSeatType(seatDetails.getSeatType());
                if (seatDetails.getCinemaRoom() != null) {
                    seat.setCinemaRoom(seatDetails.getCinemaRoom());
                }
                session.update(seat);
                transaction.commit();
                System.out.println("Seat updated successfully with ID: " + seatId);
            } else {
                System.out.println("Seat not found with ID: " + seatId);
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error updating seat: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    /**
     * Delete seat by ID
     */
    public void deleteSeatById(Integer seatId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Seat seat = session.get(Seat.class, seatId);
            if (seat != null) {
                session.delete(seat);
                transaction.commit();
                System.out.println("Seat deleted successfully with ID: " + seatId);
            } else {
                System.out.println("Seat not found with ID: " + seatId);
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error deleting seat: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    /**
     * Count total seats
     */
    public Long countAllSeats() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Long count = 0L;
        try {
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM Seat", Long.class);
            count = query.getSingleResult();
        } catch (Exception e) {
            System.err.println("Error counting seats: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
        return count;
    }
}
