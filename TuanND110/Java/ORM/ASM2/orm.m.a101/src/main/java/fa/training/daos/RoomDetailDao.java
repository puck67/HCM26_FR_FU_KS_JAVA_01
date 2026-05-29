package fa.training.daos;

import fa.training.entities.CinemaRoomDetail;
import fa.training.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * RoomDetailDao - Data Access Object for CinemaRoomDetail entity (RoomDetailDao)
 */
public class RoomDetailDao {

    /**
     * Insert a new cinema room detail
     */
    public Integer insertRoomDetail(CinemaRoomDetail roomDetail) {
        Objects.requireNonNull(roomDetail, "roomDetail must not be null");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        Integer detailId = null;
        try {
            transaction = session.beginTransaction();
            detailId = (Integer) session.save(roomDetail);
            transaction.commit();
            System.out.println("Cinema Room Detail inserted successfully with ID: " + detailId);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error inserting cinema room detail: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
        return detailId;
    }

    /**
     * Get cinema room detail by ID
     */
    public CinemaRoomDetail getRoomDetailById(Integer roomDetailId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        CinemaRoomDetail detail = null;
        try {
            detail = session.get(CinemaRoomDetail.class, roomDetailId);
        } catch (Exception e) {
            System.err.println("Error retrieving cinema room detail: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
        return detail;
    }

    /**
     * Get all cinema room details
     */
    public List<CinemaRoomDetail> getAllRoomDetails() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<CinemaRoomDetail> details = null;
        try {
            Query<CinemaRoomDetail> query = session.createQuery("FROM CinemaRoomDetail", CinemaRoomDetail.class);
            details = query.list();
        } catch (Exception e) {
            System.err.println("Error retrieving all cinema room details: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
        return details;
    }

    /**
     * Get room details by room rate range
     */
    public List<CinemaRoomDetail> getRoomDetailsByRateRange(Integer minRate, Integer maxRate) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<CinemaRoomDetail> details = null;
        try {
            Query<CinemaRoomDetail> query = session.createQuery(
                    "FROM CinemaRoomDetail WHERE roomRate BETWEEN :minRate AND :maxRate",
                    CinemaRoomDetail.class
            );
            query.setParameter("minRate", minRate);
            query.setParameter("maxRate", maxRate);
            details = query.list();
        } catch (Exception e) {
            System.err.println("Error retrieving room details by rate range: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
        return details;
    }

    /**
     * Get room details by active date after specified date
     */
    public List<CinemaRoomDetail> getRoomDetailsActiveAfter(LocalDate date) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<CinemaRoomDetail> details = null;
        try {
            Query<CinemaRoomDetail> query = session.createQuery(
                    "FROM CinemaRoomDetail WHERE activeDate >= :date",
                    CinemaRoomDetail.class
            );
            query.setParameter("date", date);
            details = query.list();
        } catch (Exception e) {
            System.err.println("Error retrieving room details active after date: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
        return details;
    }

    /**
     * Update cinema room detail
     */
    public void updateRoomDetailById(Integer roomDetailId, CinemaRoomDetail detailUpdates) {
        Objects.requireNonNull(roomDetailId, "roomDetailId must not be null");
        Objects.requireNonNull(detailUpdates, "detailUpdates must not be null");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            CinemaRoomDetail detail = session.get(CinemaRoomDetail.class, roomDetailId);
            if (detail != null) {
                detail.setRoomRate(detailUpdates.getRoomRate());
                detail.setActiveDate(detailUpdates.getActiveDate());
                detail.setRoomDescription(detailUpdates.getRoomDescription());
                if (detailUpdates.getCinemaRoom() != null) {
                    detail.setCinemaRoom(detailUpdates.getCinemaRoom());
                }
                session.update(detail);
                transaction.commit();
                System.out.println("Cinema Room Detail updated successfully with ID: " + roomDetailId);
            } else {
                System.out.println("Cinema Room Detail not found with ID: " + roomDetailId);
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error updating cinema room detail: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    /**
     * Delete cinema room detail by ID
     */
    public void deleteRoomDetailById(Integer roomDetailId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            CinemaRoomDetail detail = session.get(CinemaRoomDetail.class, roomDetailId);
            if (detail != null) {
                session.delete(detail);
                transaction.commit();
                System.out.println("Cinema Room Detail deleted successfully with ID: " + roomDetailId);
            } else {
                System.out.println("Cinema Room Detail not found with ID: " + roomDetailId);
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error deleting cinema room detail: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    /**
     * Count total cinema room details
     */
    public Long countAllRoomDetails() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Long count = 0L;
        try {
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM CinemaRoomDetail", Long.class);
            count = query.getSingleResult();
        } catch (Exception e) {
            System.err.println("Error counting cinema room details: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
        return count;
    }
}
