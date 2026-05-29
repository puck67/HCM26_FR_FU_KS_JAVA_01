package fa.training.dao;

import fa.training.entities.CinemaRoom;
import fa.training.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import jakarta.persistence.PersistenceException;
import java.util.Collections;
import java.util.List;

public class RoomDaoImpl implements RoomDao {

    @Override
    public boolean insertRoom(CinemaRoom room) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(room);
            transaction.commit();
            return true;
        } catch (PersistenceException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Insert Room failed (Database/Mapping error): " + e.getMessage());
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Insert Room failed (System error): " + e.getMessage());
            return false;
        }
    }

    @Override
    public CinemaRoom getRoomById(int id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery(
                "SELECT r FROM CinemaRoom r " +
                "LEFT JOIN FETCH r.seats " +
                "LEFT JOIN FETCH r.cinemaRoomDetail " +
                "WHERE r.cinemaRoomId = :id", CinemaRoom.class)
                .setParameter("id", id)
                .uniqueResult();
        } catch (PersistenceException e) {
            System.err.println("Get Room by ID failed (Database error): " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Get Room by ID failed: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<CinemaRoom> getAllRoom() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("FROM CinemaRoom", CinemaRoom.class).list();
        } catch (PersistenceException e) {
            System.err.println("Get All Rooms failed (Database error): " + e.getMessage());
            return Collections.emptyList();
        } catch (Exception e) {
            System.err.println("Get All Rooms failed: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public boolean updateRoomById(CinemaRoom room) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            CinemaRoom existing = session.get(CinemaRoom.class, room.getCinemaRoomId());
            if (existing != null) {
                session.merge(room);
                transaction.commit();
                return true;
            }
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        } catch (PersistenceException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Update Room failed (Database/Mapping error): " + e.getMessage());
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Update Room failed: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteRoomById(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            CinemaRoom existing = session.get(CinemaRoom.class, id);
            if (existing != null) {
                session.remove(existing);
                transaction.commit();
                return true;
            }
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        } catch (PersistenceException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Delete Room failed (Database error): " + e.getMessage());
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Delete Room failed: " + e.getMessage());
            return false;
        }
    }
}
