package fa.training.dao;

import fa.training.entities.CinemaRoomDetail;
import fa.training.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import jakarta.persistence.PersistenceException;
import java.util.Collections;
import java.util.List;

public class RoomDetailDaoImpl implements RoomDetailDao {

    @Override
    public boolean insertRoomDetail(CinemaRoomDetail roomDetail) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(roomDetail);
            transaction.commit();
            return true;
        } catch (PersistenceException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Insert RoomDetail failed (Database/Mapping error): " + e.getMessage());
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Insert RoomDetail failed (System error): " + e.getMessage());
            return false;
        }
    }

    @Override
    public CinemaRoomDetail getRoomDetailById(int id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("SELECT d FROM CinemaRoomDetail d LEFT JOIN FETCH d.cinemaRoom WHERE d.cinemaRoomDetailId = :id", CinemaRoomDetail.class)
                    .setParameter("id", id)
                    .uniqueResult();
        } catch (PersistenceException e) {
            System.err.println("Get RoomDetail by ID failed (Database error): " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Get RoomDetail by ID failed: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<CinemaRoomDetail> getAllRoomDetail() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("SELECT d FROM CinemaRoomDetail d LEFT JOIN FETCH d.cinemaRoom", CinemaRoomDetail.class).list();
        } catch (PersistenceException e) {
            System.err.println("Get All RoomDetails failed (Database error): " + e.getMessage());
            return Collections.emptyList();
        } catch (Exception e) {
            System.err.println("Get All RoomDetails failed: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public boolean updateRoomDetailById(CinemaRoomDetail roomDetail) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            CinemaRoomDetail existing = session.get(CinemaRoomDetail.class, roomDetail.getCinemaRoomDetailId());
            if (existing != null) {
                session.merge(roomDetail);
                transaction.commit();
                return true;
            }
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        } catch (PersistenceException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Update RoomDetail failed (Database/Mapping error): " + e.getMessage());
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Update RoomDetail failed: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteRoomDetailById(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            CinemaRoomDetail existing = session.get(CinemaRoomDetail.class, id);
            if (existing != null) {
                session.remove(existing);
                transaction.commit();
                return true;
            }
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        } catch (PersistenceException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Delete RoomDetail failed (Database error): " + e.getMessage());
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Delete RoomDetail failed: " + e.getMessage());
            return false;
        }
    }
}
