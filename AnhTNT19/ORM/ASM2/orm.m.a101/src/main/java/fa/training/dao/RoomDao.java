package fa.training.dao;

import fa.training.entities.CinemaRoom;
import fa.training.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class RoomDao {

    public boolean insertRoom(CinemaRoom room) {
        Transaction tx = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.save(room);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public CinemaRoom getRoomById(int id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(CinemaRoom.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<CinemaRoom> getAllRooms() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("FROM CinemaRoom", CinemaRoom.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean updateRoomById(int id, String newName, int newQuantity) {
        Transaction tx = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            CinemaRoom room = session.get(CinemaRoom.class, id);
            if (room == null) return false;
            room.setCinemaRoomName(newName);
            room.setSeatQuantity(newQuantity);
            session.update(room);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteRoomById(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            CinemaRoom room = session.get(CinemaRoom.class, id);
            if (room == null) return false;
            session.delete(room);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public CinemaRoom getRoomByName(String name) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM CinemaRoom cr WHERE cr.cinemaRoomName = :name", CinemaRoom.class)
                    .setParameter("name", name)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}