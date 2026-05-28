package fa.training.dao.impl;

import fa.training.dao.RoomDao;
import fa.training.entities.CinemaRoom;
import fa.training.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class RoomDaoImpl implements RoomDao {

    @Override
    public boolean insertRoom(CinemaRoom room) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(room);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public CinemaRoom getRoomById(int roomId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "select distinct r from CinemaRoom r left join fetch r.seats left join fetch r.cinemaRoomDetail where r.cinemaRoomId = :roomId", 
                CinemaRoom.class)
                .setParameter("roomId", roomId)
                .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<CinemaRoom> getAllRooms() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "select distinct r from CinemaRoom r left join fetch r.seats left join fetch r.cinemaRoomDetail", 
                CinemaRoom.class)
                .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean updateRoomById(CinemaRoom room) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(room);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteRoomById(int roomId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            CinemaRoom room = session.get(CinemaRoom.class, roomId);
            if (room != null) {
                session.delete(room);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }
}
