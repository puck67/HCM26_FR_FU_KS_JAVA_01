package fa.training.dao;

import fa.training.entities.CinemaRoom;
import fa.training.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

/**
 * DAO for CinemaRoom entity.
 *
 * Naming convention consistent across all DAO classes:
 *   findXxxById   / findAllXxx   / saveXxx
 *   updateXxxById / deleteXxxById
 *
 * Every Session is opened via try-with-resources to prevent connection leaks.
 * Scanner / System.out are NEVER used here — UI concerns belong in Main only.
 */
public class RoomDao {

    public Optional<CinemaRoom> findRoomById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(CinemaRoom.class, id));
        }
    }

    public List<CinemaRoom> findAllRooms() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<CinemaRoom> query = session.createQuery(
                    "FROM CinemaRoom ORDER BY cinemaRoomId", CinemaRoom.class);
            return query.list();
        }
    }

    public CinemaRoom saveRoom(CinemaRoom room) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.save(room);
            tx.commit();
            return room;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Failed to save CinemaRoom", e);
        }
    }

    /**
     * Update name and seat quantity of a room by its id.
     *
     * @return true if found and updated, false if id does not exist
     */
    public boolean updateRoomById(int id, String newName, int newSeatQuantity) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            CinemaRoom room = session.get(CinemaRoom.class, id);
            if (room == null) {
                tx.rollback();
                return false;
            }
            room.setCinemaRoomName(newName);
            room.setSeatQuantity(newSeatQuantity);
            session.update(room);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Failed to update CinemaRoom id=" + id, e);
        }
    }

    /**
     * Delete a room (and cascades to its detail + seats).
     *
     * @return true if found and deleted, false if id does not exist
     */
    public boolean deleteRoomById(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            CinemaRoom room = session.get(CinemaRoom.class, id);
            if (room == null) {
                tx.rollback();
                return false;
            }
            session.delete(room);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Failed to delete CinemaRoom id=" + id, e);
        }
    }
}
