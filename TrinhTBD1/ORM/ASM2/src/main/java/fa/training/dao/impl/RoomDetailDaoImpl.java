package fa.training.dao.impl;

import fa.training.dao.RoomDetailDao;
import fa.training.entities.CinemaRoomDetail;
import fa.training.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.Collections;
import java.util.List;

public class RoomDetailDaoImpl implements RoomDetailDao {

    @Override
    public boolean insertRoomDetail(CinemaRoomDetail roomDetail) {
        if (roomDetail == null) {
            System.err.println(new StringBuilder().append("Error: Room detail is null."));
            return false;
        }
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(roomDetail);
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
                    .append("Error inserting room detail: ")
                    .append(e.getMessage()));
            return false;
        }
    }

    @Override
    public CinemaRoomDetail getRoomDetailByID(int id) {
        if (id <= 0) {
            System.err.println(new StringBuilder().append("Error: Invalid ID."));
            return null;
        }
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(CinemaRoomDetail.class, id);
        } catch (Exception e) {
            System.err.println(new StringBuilder()
                    .append("Error getting room detail by ID: ")
                    .append(e.getMessage()));
            return null;
        }
    }

    @Override
    public List<CinemaRoomDetail> getAllRoomDetail() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            StringBuilder hql = new StringBuilder("from CinemaRoomDetail");
            Query<CinemaRoomDetail> query = session.createQuery(hql.toString(), CinemaRoomDetail.class);
            return query.list();
        } catch (Exception e) {
            System.err.println(new StringBuilder()
                    .append("Error getting all room details: ")
                    .append(e.getMessage()));
            return Collections.emptyList();
        }
    }

    @Override
    public boolean updateRoomDetailByID(int id, CinemaRoomDetail roomDetail) {
        if (id <= 0 || roomDetail == null) {
            System.err.println(new StringBuilder().append("Error: Invalid update arguments."));
            return false;
        }
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            CinemaRoomDetail existing = session.get(CinemaRoomDetail.class, id);
            if (existing != null) {
                existing.setRoomRate(roomDetail.getRoomRate());
                existing.setActiveDate(roomDetail.getActiveDate());
                existing.setRoomDescription(roomDetail.getRoomDescription());
                if (roomDetail.getCinemaRoom() != null) {
                    existing.setCinemaRoom(roomDetail.getCinemaRoom());
                }
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
                    .append("Error updating room detail by ID: ")
                    .append(e.getMessage()));
            return false;
        }
    }

    @Override
    public boolean deleteRoomDetailById(int id) {
        if (id <= 0) {
            System.err.println(new StringBuilder().append("Error: Invalid ID."));
            return false;
        }
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            CinemaRoomDetail roomDetail = session.get(CinemaRoomDetail.class, id);
            if (roomDetail != null) {
                if (roomDetail.getCinemaRoom() != null) {
                    roomDetail.getCinemaRoom().setCinemaRoomDetail(null);
                    session.merge(roomDetail.getCinemaRoom());
                }
                session.remove(roomDetail);
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
                    .append("Error deleting room detail by ID: ")
                    .append(e.getMessage()));
            return false;
        }
    }
}
