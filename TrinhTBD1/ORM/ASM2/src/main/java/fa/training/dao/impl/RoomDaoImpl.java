package fa.training.dao.impl;

import fa.training.dao.RoomDao;
import fa.training.entities.CinemaRoom;
import fa.training.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.Collections;
import java.util.List;

public class RoomDaoImpl implements RoomDao {

    @Override
    public boolean insertRoom(CinemaRoom room) {
        if (room == null) {
            System.err.println(new StringBuilder().append("Error: Room is null."));
            return false;
        }
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(room);
            transaction.commit();
            return true;
        } catch (Exception e) {
            try {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
            } catch (Exception rollbackEx) {
            }
            System.err.println(new StringBuilder()
                    .append("Error inserting room: ")
                    .append(e.getMessage()));
            return false;
        }
    }

    @Override
    public CinemaRoom getRoomByID(int id) {
        if (id <= 0) {
            System.err.println(new StringBuilder().append("Error: Invalid ID."));
            return null;
        }
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(CinemaRoom.class, id);
        } catch (Exception e) {
            System.err.println(new StringBuilder()
                    .append("Error getting room by ID: ")
                    .append(e.getMessage()));
            return null;
        }
    }

    @Override
    public List<CinemaRoom> getAllRoom() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            StringBuilder hql = new StringBuilder("from CinemaRoom");
            Query<CinemaRoom> query = session.createQuery(hql.toString(), CinemaRoom.class);
            return query.list();
        } catch (Exception e) {
            System.err.println(new StringBuilder()
                    .append("Error getting all rooms: ")
                    .append(e.getMessage()));
            return Collections.emptyList();
        }
    }

    @Override
    public boolean updateRoomByID(int id, CinemaRoom room) {
        if (id <= 0 || room == null) {
            System.err.println(new StringBuilder().append("Error: Invalid update arguments."));
            return false;
        }
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            CinemaRoom existing = session.get(CinemaRoom.class, id);
            if (existing != null) {
                existing.setCinemaRoomName(room.getCinemaRoomName());
                existing.setSeatQuantity(room.getSeatQuantity());
                session.merge(existing);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            try {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
            } catch (Exception rollbackEx) {
            }
            System.err.println(new StringBuilder()
                    .append("Error updating room by ID: ")
                    .append(e.getMessage()));
            return false;
        }
    }

    @Override
    public boolean deleteRoomById(int id) {
        if (id <= 0) {
            System.err.println(new StringBuilder().append("Error: Invalid ID."));
            return false;
        }
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
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
            } catch (Exception rollbackEx) {
            }
            System.err.println(new StringBuilder()
                    .append("Error deleting room by ID: ")
                    .append(e.getMessage()));
            return false;
        }
    }
}
