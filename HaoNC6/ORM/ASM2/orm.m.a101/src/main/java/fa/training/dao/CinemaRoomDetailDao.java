package fa.training.dao;

import fa.training.entities.CinemaRoomDetail;
import fa.training.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class CinemaRoomDetailDao {

    // 1. Insert
    public void insertCinemaRoomDetail(CinemaRoomDetail detail) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(detail);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // 2. Get By ID
    public CinemaRoomDetail getCinemaRoomDetailById(int id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(CinemaRoomDetail.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 3. Get All
    public List<CinemaRoomDetail> getAllCinemaRoomDetail() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from CinemaRoomDetail", CinemaRoomDetail.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 4. Update By ID
    public void updateCinemaRoomDetailById(CinemaRoomDetail updatedDetail) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(updatedDetail);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // 5. Delete By ID
    public void deleteCinemaRoomDetailById(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            CinemaRoomDetail detail = session.get(CinemaRoomDetail.class, id);
            if (detail != null) {
                session.delete(detail);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
}