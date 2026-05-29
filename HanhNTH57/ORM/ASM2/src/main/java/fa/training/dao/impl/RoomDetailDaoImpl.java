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
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(roomDetail);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public CinemaRoomDetail getRoomDetailById(int id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(CinemaRoomDetail.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<CinemaRoomDetail> getAllRoomDetails() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Query<CinemaRoomDetail> query = session.createQuery("FROM CinemaRoomDetail", CinemaRoomDetail.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public boolean updateRoomDetailById(int id, CinemaRoomDetail roomDetail) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            CinemaRoomDetail existingDetail = session.get(CinemaRoomDetail.class, id);
            if (existingDetail != null) {
                existingDetail.setRoomRate(roomDetail.getRoomRate());
                existingDetail.setActiveDate(roomDetail.getActiveDate());
                existingDetail.setRoomDescription(roomDetail.getRoomDescription());
                session.update(existingDetail);
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
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public boolean deleteRoomDetailById(int id) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            CinemaRoomDetail roomDetail = session.get(CinemaRoomDetail.class, id);
            if (roomDetail != null) {
                // Decouple from CinemaRoom to avoid "deleted object would be re-saved by cascade"
                if (roomDetail.getCinemaRoom() != null) {
                    roomDetail.getCinemaRoom().setCinemaRoomDetail(null);
                }
                session.delete(roomDetail);
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
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
