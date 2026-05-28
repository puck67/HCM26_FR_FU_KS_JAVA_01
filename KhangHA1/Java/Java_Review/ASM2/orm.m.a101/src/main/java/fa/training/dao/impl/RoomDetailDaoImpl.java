package fa.training.dao.impl;

import fa.training.dao.RoomDetailDao;
import fa.training.entities.CinemaRoomDetail;
import fa.training.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class RoomDetailDaoImpl implements RoomDetailDao {

    @Override
    public boolean insertRoomDetail(CinemaRoomDetail detail) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(detail);
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
    public CinemaRoomDetail getRoomDetailById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(CinemaRoomDetail.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<CinemaRoomDetail> getAllRoomDetails() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from CinemaRoomDetail", CinemaRoomDetail.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean updateRoomDetailById(CinemaRoomDetail detail) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(detail);
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
    public boolean deleteRoomDetailById(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            CinemaRoomDetail detail = session.get(CinemaRoomDetail.class, id);
            if (detail != null) {
                session.remove(detail);
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
