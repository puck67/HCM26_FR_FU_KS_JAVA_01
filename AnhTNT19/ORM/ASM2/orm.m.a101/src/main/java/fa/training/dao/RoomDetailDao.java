package fa.training.dao;

import fa.training.entities.CinemaRoomDetail;
import fa.training.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.time.LocalDate;
import java.util.List;

public class RoomDetailDao {

    public boolean insertDetail(CinemaRoomDetail detail) {
        Transaction tx = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.save(detail);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public CinemaRoomDetail getDetailById(int id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(CinemaRoomDetail.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<CinemaRoomDetail> getAllDetails() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("FROM CinemaRoomDetail", CinemaRoomDetail.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean updateDetailById(int id, int newRate, LocalDate newActiveDate, String newDescription) {
        Transaction tx = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            CinemaRoomDetail detail = session.get(CinemaRoomDetail.class, id);
            if (detail == null) return false;
            detail.setRoomRate(newRate);
            detail.setActiveDate(newActiveDate);
            detail.setRoomDescription(newDescription);
            session.update(detail);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteDetailById(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            CinemaRoomDetail detail = session.get(CinemaRoomDetail.class, id);
            if (detail == null) return false;
            session.delete(detail);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public List<CinemaRoomDetail> getDetailsByActiveDateFrom(LocalDate fromDate) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM CinemaRoomDetail d WHERE d.activeDate >= :fromDate",
                            CinemaRoomDetail.class)
                    .setParameter("fromDate", fromDate)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}