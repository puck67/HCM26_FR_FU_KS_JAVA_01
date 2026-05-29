package fa.training.dao;

import fa.training.entities.CinemaRoom;
import fa.training.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * DAO for CinemaRoom entity.
 * Provides 5 CRUD operations: insert, getByID, getAll, updateByID, deleteByID.
 */
public class RoomDAO {

    /**
     * Insert a new CinemaRoom (and its cascaded Seats + Detail if set).
     */
    public void insertRoom(CinemaRoom room) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(room);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    /**
     * Find a CinemaRoom by its ID.
     */
    public CinemaRoom getRoomByID(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(CinemaRoom.class, id);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get all CinemaRooms.
     */
    public List<CinemaRoom> getAllRooms() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM CinemaRoom", CinemaRoom.class).list();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Update name and seatQuantity of a CinemaRoom by ID.
     */
    public void updateRoomByID(int id, String newName, int newSeatQuantity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            CinemaRoom room = session.get(CinemaRoom.class, id);
            if (room != null) {
                room.setCinemaRoomName(newName);
                room.setSeatQuantity(newSeatQuantity);
                session.update(room);
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    /**
     * Delete a CinemaRoom by ID (cascades to Seats and Detail).
     */
    public void deleteRoomByID(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            CinemaRoom room = session.get(CinemaRoom.class, id);
            if (room != null) {
                session.delete(room);
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    /**
     * Check if a room name already exists in the database.
     * @param name The room name to check.
     * @param excludeRoomId The ID of the room to exclude from the check (used for updates). Null if creating new.
     * @return true if the name exists, false otherwise.
     */
    public boolean isRoomNameExists(String name, Integer excludeRoomId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT count(c) FROM CinemaRoom c WHERE c.cinemaRoomName = :name";
            if (excludeRoomId != null) {
                hql += " AND c.cinemaRoomId != :excludeId";
            }
            org.hibernate.query.Query<Long> query = session.createQuery(hql, Long.class)
                               .setParameter("name", name);
            if (excludeRoomId != null) {
                query.setParameter("excludeId", excludeRoomId);
            }
            Long count = query.uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
