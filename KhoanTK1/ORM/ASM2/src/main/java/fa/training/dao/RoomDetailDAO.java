package fa.training.dao;

import fa.training.entities.CinemaRoomDetail;
import fa.training.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomDetailDAO {

    public List<CinemaRoomDetail> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM CinemaRoomDetail", CinemaRoomDetail.class).list();
        } catch (Exception e) {
            System.err.println("Loi lay toan bo chi tiet phong: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public Optional<CinemaRoomDetail> getById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CinemaRoomDetail detail = session.get(CinemaRoomDetail.class, id);
            return Optional.ofNullable(detail);
        } catch (Exception e) {
            System.err.println("Loi tim chi tiet phong bang ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<CinemaRoomDetail> getByRoomId(int roomId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CinemaRoomDetail detail = session.createQuery(
                    "FROM CinemaRoomDetail d WHERE d.cinemaRoom.cinemaRoomId = :roomId", CinemaRoomDetail.class)
                    .setParameter("roomId", roomId)
                    .uniqueResultOptional()
                    .orElse(null);
            return Optional.ofNullable(detail);
        } catch (Exception e) {
            System.err.println("Loi tim chi tiet phong bang Room ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    public void insert(CinemaRoomDetail detail) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.save(detail);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("Loi them chi tiet phong: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void update(CinemaRoomDetail detail) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.update(detail);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("Loi cap nhat chi tiet phong: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void deleteById(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            CinemaRoomDetail detail = session.get(CinemaRoomDetail.class, id);
            if (detail != null) {
                session.delete(detail);
                tx.commit();
            } else {
                if (tx != null && tx.isActive()) {
                    tx.rollback();
                }
                throw new IllegalArgumentException("Chi tiet phong voi ID=" + id + " khong ton tai de xoa.");
            }
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("Loi xoa chi tiet phong: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
