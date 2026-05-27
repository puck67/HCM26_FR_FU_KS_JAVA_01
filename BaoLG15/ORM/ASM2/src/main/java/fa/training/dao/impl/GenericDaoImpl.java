package fa.training.dao.impl;

import fa.training.dao.GenericDao;
import fa.training.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class GenericDaoImpl<T, ID> implements GenericDao<T, ID> {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    private final Class<T> entityClass;

    protected GenericDaoImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public Optional<T> getById(ID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            T entity = session.get(entityClass, (Serializable) id);
            return Optional.ofNullable(entity);
        } catch (Exception ex) {
            log.error("Error in getById for {} with id={}: {}", entityClass.getSimpleName(), id, ex.getMessage(), ex);
            return Optional.empty();
        }
    }

    @Override
    public List<T> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM " + entityClass.getSimpleName(), entityClass).list();
        } catch (Exception ex) {
            log.error("Error in getAll for {}: {}", entityClass.getSimpleName(), ex.getMessage(), ex);
            return Collections.emptyList();
        }
    }

    @Override
    public void insert(T entity) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.save(entity);
            tx.commit();
            log.info("Inserted {}: {}", entityClass.getSimpleName(), entity);
        } catch (Exception ex) {
            if (tx != null)
                tx.rollback();
            log.error("Error in insert for {}: {}", entityClass.getSimpleName(), ex.getMessage(), ex);
            throw new RuntimeException("Insert failed: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void updateById(T entity) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.update(entity);
            tx.commit();
            log.info("Updated {}: {}", entityClass.getSimpleName(), entity);
        } catch (Exception ex) {
            if (tx != null)
                tx.rollback();
            log.error("Error in updateById for {}: {}", entityClass.getSimpleName(), ex.getMessage(), ex);
            throw new RuntimeException("Update failed: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void deleteById(ID id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            T entity = session.get(entityClass, (java.io.Serializable) id);
            if (entity != null) {
                session.delete(entity);
                tx.commit();
                log.info("Deleted {} with id={}", entityClass.getSimpleName(), id);
            } else {
                tx.rollback();
                log.warn("deleteById: {} with id={} not found.", entityClass.getSimpleName(), id);
                throw new RuntimeException(entityClass.getSimpleName() + " with id=" + id + " not found.");
            }
        } catch (RuntimeException ex) {
            if (tx != null && tx.isActive())
                tx.rollback();
            log.error("Error in deleteById for {} id={}: {}", entityClass.getSimpleName(), id, ex.getMessage(), ex);
            throw ex;
        }
    }
}
