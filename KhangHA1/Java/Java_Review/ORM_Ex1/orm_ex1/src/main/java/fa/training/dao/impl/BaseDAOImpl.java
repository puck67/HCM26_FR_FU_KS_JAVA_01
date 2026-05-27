package fa.training.dao.impl;

import fa.training.dao.BaseDAO;
import fa.training.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract Hibernate-backed base implementation of BaseDAO.
 * Subclasses only need to provide the entity class; special delete behaviour
 * (e.g. clearing join-table entries) can be overridden.
 *
 * @param <T>  entity type
 * @param <ID> primary-key type
 */
public abstract class BaseDAOImpl<T, ID> implements BaseDAO<T, ID> {

    /** Return the JPA entity class, e.g. {@code Student.class}. */
    protected abstract Class<T> getEntityClass();

    // ------------------------------------------------------------------ //
    //  CREATE
    // ------------------------------------------------------------------ //

    @Override
    public boolean add(T entity) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(entity);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("[DAO] Error adding " + getEntityClass().getSimpleName()
                    + ": " + e.getMessage());
            return false;
        }
    }

    // ------------------------------------------------------------------ //
    //  READ
    // ------------------------------------------------------------------ //

    @Override
    public List<T> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM " + getEntityClass().getSimpleName(), getEntityClass()
            ).list();
        } catch (Exception e) {
            System.err.println("[DAO] Error getting all "
                    + getEntityClass().getSimpleName() + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public T findById(ID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(getEntityClass(), id);
        } catch (Exception e) {
            System.err.println("[DAO] Error finding by id: " + e.getMessage());
            return null;
        }
    }

    // ------------------------------------------------------------------ //
    //  UPDATE
    // ------------------------------------------------------------------ //

    @Override
    public boolean update(T entity) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(entity);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("[DAO] Error updating " + getEntityClass().getSimpleName()
                    + ": " + e.getMessage());
            return false;
        }
    }

    // ------------------------------------------------------------------ //
    //  DELETE  (subclasses may override to handle join-table cleanup)
    // ------------------------------------------------------------------ //

    @Override
    public boolean delete(ID id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            T entity = session.get(getEntityClass(), id);
            if (entity == null) {
                tx.rollback();
                return false;
            }
            session.remove(entity);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("[DAO] Error deleting " + getEntityClass().getSimpleName()
                    + " id=" + id + ": " + e.getMessage());
            return false;
        }
    }
}
