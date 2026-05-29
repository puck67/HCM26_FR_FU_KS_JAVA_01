package fa.training.dao;

import fa.training.entities.CinemaRoom;
import fa.training.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomDAO {

    public List<CinemaRoom> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM CinemaRoom", CinemaRoom.class).list();
        } catch (Exception e) {
            System.err.println("Loi lay toan bo phong chieu: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public Optional<CinemaRoom> getById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CinemaRoom room = session.get(CinemaRoom.class, id);
            return Optional.ofNullable(room);
        } catch (Exception e) {
            System.err.println("Loi tim phong chieu bang ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<CinemaRoom> getByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CinemaRoom room = session.createQuery(
                    "FROM CinemaRoom r WHERE r.cinemaRoomName = :name", CinemaRoom.class)
                    .setParameter("name", name)
                    .uniqueResultOptional()
                    .orElse(null);
            return Optional.ofNullable(room);
        } catch (Exception e) {
            System.err.println("Loi tim phong chieu bang ten: " + e.getMessage());
            return Optional.empty();
        }
    }

    public void insert(CinemaRoom room) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.save(room);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("Loi them phong chieu: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void update(CinemaRoom room) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.update(room);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("Loi cap nhat phong chieu: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void deleteById(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            CinemaRoom room = session.get(CinemaRoom.class, id);
            if (room != null) {
                session.delete(room);
                tx.commit();
            } else {
                if (tx != null && tx.isActive()) {
                    tx.rollback();
                }
                throw new IllegalArgumentException("Phong chieu voi ID=" + id + " khong ton tai de xoa.");
            }
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("Loi xoa phong chieu: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
