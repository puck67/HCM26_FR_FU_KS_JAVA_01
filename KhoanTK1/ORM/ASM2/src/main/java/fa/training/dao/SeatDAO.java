package fa.training.dao;

import fa.training.entities.Seat;
import fa.training.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SeatDAO {

    public List<Seat> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Seat", Seat.class).list();
        } catch (Exception e) {
            System.err.println("Loi lay toan bo ghe: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public Optional<Seat> getById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Seat seat = session.get(Seat.class, id);
            return Optional.ofNullable(seat);
        } catch (Exception e) {
            System.err.println("Loi tim ghe bang ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<Seat> getByRoomId(int roomId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Seat s WHERE s.cinemaRoom.cinemaRoomId = :roomId", Seat.class)
                    .setParameter("roomId", roomId)
                    .list();
        } catch (Exception e) {
            System.err.println("Loi tim ghe bang Room ID: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Seat> getByStatus(String status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Seat s WHERE s.seatStatus = :status", Seat.class)
                    .setParameter("status", status)
                    .list();
        } catch (Exception e) {
            System.err.println("Loi tim ghe bang status: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Seat> getByType(String type) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Seat s WHERE s.seatType = :type", Seat.class)
                    .setParameter("type", type)
                    .list();
        } catch (Exception e) {
            System.err.println("Loi tim ghe bang type: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void insert(Seat seat) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.save(seat);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("Loi them ghe: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void update(Seat seat) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.update(seat);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("Loi cap nhat ghe: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void deleteById(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Seat seat = session.get(Seat.class, id);
            if (seat != null) {
                session.delete(seat);
                tx.commit();
            } else {
                if (tx != null && tx.isActive()) {
                    tx.rollback();
                }
                throw new IllegalArgumentException("Ghe voi ID=" + id + " khong ton tai de xoa.");
            }
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("Loi xoa ghe: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
