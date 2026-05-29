package fa.training.dao;

import fa.training.entities.CinemaRoom;
import fa.training.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class CinemaRoomDao {

    // 1. Insert
    public void insertCinemaRoom(CinemaRoom cinemaRoom) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(cinemaRoom);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // 2. Get By ID
    public CinemaRoom getCinemaRoomById(int id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(CinemaRoom.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 3. Get All
    public List<CinemaRoom> getAllCinemaRoom() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from CinemaRoom", CinemaRoom.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 4. Update By ID
    public void updateCinemaRoomById(CinemaRoom updatedRoom) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(updatedRoom);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // 5. Delete By ID
    public void deleteCinemaRoomById(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            CinemaRoom room = session.get(CinemaRoom.class, id);
            if (room != null) {
                session.delete(room);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
}