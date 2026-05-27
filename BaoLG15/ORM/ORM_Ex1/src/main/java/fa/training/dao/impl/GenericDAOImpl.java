package fa.training.dao.impl;

import fa.training.dao.GenericDAO;
import fa.training.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class GenericDAOImpl<T, ID> implements GenericDAO<T, ID> {

    private static final Logger logger = LoggerFactory.getLogger(GenericDAOImpl.class);

    protected final Class<T> entityType;

    public GenericDAOImpl(Class<T> entityType) {
        this.entityType = entityType;
    }

    @Override
    public void save(T entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error occurred while saving entity of type {}", entityType.getSimpleName(), e);
        }
    }

    @Override
    public void update(T entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(entity);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error occurred while updating entity of type {}", entityType.getSimpleName(), e);
        }
    }

    @Override
    public void delete(ID id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            T entity = session.get(entityType, id);
            if (entity != null) {
                cleanupBeforeDelete(session, entity);
                session.remove(entity);
            }
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error occurred while deleting entity of type {} with ID {}", entityType.getSimpleName(), id, e);
        }
    }

    protected void cleanupBeforeDelete(Session session, T entity) {
    }

    @Override
    public T findById(ID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(entityType, id);
        }
    }

    @Override
    public List<T> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM " + entityType.getSimpleName(), entityType).list();
        }
    }
}
