package fa.training.dao;

import fa.training.entities.CinemaRoom;
import fa.training.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class RoomDao {

    public Optional<CinemaRoom> findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(CinemaRoom.class, id));
        }
    }

    public List<CinemaRoom> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM CinemaRoom", CinemaRoom.class).list();
        }
    }

    public void save(CinemaRoom room) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                session.persist(room);
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        }
    }

    public void update(CinemaRoom room) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                session.merge(room);
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        }
    }

    public void deleteById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                deleteSeatsOf(session, id);
                deleteDetailOf(session, id);
                CinemaRoom room = session.get(CinemaRoom.class, id);
                if (room != null) {
                    session.delete(room);
                }
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        }
    }

    private void deleteSeatsOf(Session session, int roomId) {
        session.createQuery("DELETE FROM Seat WHERE cinemaRoom.cinemaRoomId = :rid")
               .setParameter("rid", roomId)
               .executeUpdate();
    }

    private void deleteDetailOf(Session session, int roomId) {
        session.createQuery("DELETE FROM CinemaRoomDetail WHERE cinemaRoom.cinemaRoomId = :rid")
               .setParameter("rid", roomId)
               .executeUpdate();
    }
}
