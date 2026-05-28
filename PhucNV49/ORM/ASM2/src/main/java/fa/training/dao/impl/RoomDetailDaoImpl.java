package fa.training.dao.impl;

import fa.training.dao.RoomDetailDao;
import fa.training.entities.CinemaRoomDetail;
import fa.training.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.ArrayList;
import java.util.List;

public class RoomDetailDaoImpl implements RoomDetailDao {
    @Override
    public boolean insertRoomDetail(CinemaRoomDetail roomDetail) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(roomDetail);
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
            System.err.println("Error saving room detail: " + e.getMessage());
            return false;
        }
    }

    @Override
    public CinemaRoomDetail getRoomDetailByID(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(CinemaRoomDetail.class, id);
        } catch (Exception e) {
            System.err.println("Error retrieving room detail by ID: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<CinemaRoomDetail> getAllRoomDetail() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from CinemaRoomDetail", CinemaRoomDetail.class).list();
        } catch (Exception e) {
            System.err.println("Error listing room details: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public boolean updateRoomDetailByID(CinemaRoomDetail roomDetail) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(roomDetail);
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
            System.err.println("Error updating room detail: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteRoomDetailById(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            CinemaRoomDetail roomDetail = session.get(CinemaRoomDetail.class, id);
            if (roomDetail != null) {
                if (roomDetail.getCinemaRoom() != null) {
                    roomDetail.getCinemaRoom().setCinemaRoomDetailHelper(null);
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
            } catch (Exception ex) {
                System.err.println("Rollback failed: " + ex.getMessage());
            }
            System.err.println("Error deleting room detail: " + e.getMessage());
            return false;
        }
    }
}
