package com.example.dao.impl;

import com.example.dao.GenericDAO;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class GenericDAOImpl<T, ID> implements GenericDAO<T, ID> {

    private static final Logger logger = LoggerFactory.getLogger(GenericDAOImpl.class);
    private final Class<T> entityClass;

    protected GenericDAOImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public void save(T entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error saving entity of type {}", entityClass.getSimpleName(), e);
            throw new RuntimeException("Failed to save entity: " + e.getMessage(), e);
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
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error updating entity of type {}", entityClass.getSimpleName(), e);
            throw new RuntimeException("Failed to update entity: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(ID id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            T entity = session.get(entityClass, id);
            if (entity != null) {
                session.remove(entity);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error deleting entity of type {} by ID {}", entityClass.getSimpleName(), id, e);
            throw new RuntimeException("Failed to delete entity: " + e.getMessage(), e);
        }
    }

    @Override
    public T findById(ID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(entityClass, id);
        } catch (Exception e) {
            logger.error("Error finding entity of type {} by ID {}", entityClass.getSimpleName(), id, e);
            throw new RuntimeException("Failed to find entity: " + e.getMessage(), e);
        }
    }

    @Override
    public List<T> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from " + entityClass.getName(), entityClass).list();
        } catch (Exception e) {
            logger.error("Error finding all entities of type {}", entityClass.getSimpleName(), e);
            throw new RuntimeException("Failed to find all entities: " + e.getMessage(), e);
        }
    }
}
