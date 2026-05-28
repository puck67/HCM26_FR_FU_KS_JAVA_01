package fa.training.dao.impl;

import fa.training.dao.RoomDao;
import fa.training.entities.CinemaRoom;
import fa.training.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.ArrayList;
import java.util.List;

public class RoomDaoImpl implements RoomDao {
    @Override
    public boolean insertRoom(CinemaRoom room) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(room);
            transaction.commit();
            return true;
        } catch (Exception e) {
            try {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
            } catch (Exception ex) {
                System.err.println("Rollback failed: " + ex.getMessage());
            }
            // Log error internally to prevent crash
            System.err.println("Error saving room: " + e.getMessage());
            return false;
        }
    }

    @Override
    public CinemaRoom getRoomByID(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(CinemaRoom.class, id);
        } catch (Exception e) {
            System.err.println("Error retrieving room by ID: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<CinemaRoom> getAllRoom() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from CinemaRoom", CinemaRoom.class).list();
        } catch (Exception e) {
            System.err.println("Error listing rooms: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public boolean updateRoomByID(CinemaRoom room) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(room);
            transaction.commit();
            return true;
        } catch (Exception e) {
            try {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
            } catch (Exception ex) {
                System.err.println("Rollback failed: " + ex.getMessage());
            }
            System.err.println("Error updating room: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteRoomById(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            CinemaRoom room = session.get(CinemaRoom.class, id);
            if (room != null) {
                session.remove(room);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            try {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
            } catch (Exception ex) {
                System.err.println("Rollback failed: " + ex.getMessage());
            }
            System.err.println("Error deleting room: " + e.getMessage());
            return false;
        }
    }
}
