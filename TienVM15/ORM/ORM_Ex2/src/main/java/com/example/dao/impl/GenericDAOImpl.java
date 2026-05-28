package com.example.dao.impl;

import com.example.dao.GenericDAO;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * GenericDAOImpl provides default CRUD implementations using Hibernate sessions.
 * Transaction management is centralized here; subclasses extend behavior via hooks.
 *
 * @param <T>  Entity type
 * @param <ID> Primary key type
 */
public class GenericDAOImpl<T, ID extends Serializable> implements GenericDAO<T, ID> {

    protected final Class<T> entityType;

    public GenericDAOImpl(Class<T> entityType) {
        this.entityType = entityType;
    }

    /**
     * Hook for subclasses to initialize lazy collections after loading an entity.
     * Called inside an active session context.
     */
    protected void initLazyCollections(T entity) {
        // Default: no-op
    }

    /**
     * Hook for subclasses to run pre-deletion logic (e.g., dissociate relationships).
     * Called inside an active transaction before session.remove().
     */
    protected void beforeDelete(T entity) {
        // Default: no-op
    }

    @Override
    public void save(T entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void update(T entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void delete(ID id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            T entity = session.get(entityType, id);
            if (entity != null) {
                beforeDelete(entity);
                session.remove(entity);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public T findById(ID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            T entity = session.get(entityType, id);
            if (entity != null) {
                initLazyCollections(entity);
            }
            return entity;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<T> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM " + entityType.getSimpleName();
            List<T> entities = session.createQuery(hql, entityType).list();
            for (T entity : entities) {
                initLazyCollections(entity);
            }
            return entities;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
