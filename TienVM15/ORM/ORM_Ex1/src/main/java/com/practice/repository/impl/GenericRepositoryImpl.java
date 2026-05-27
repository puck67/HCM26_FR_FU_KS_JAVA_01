package com.practice.repository.impl;

import com.practice.config.HibernateUtil;
import com.practice.repository.GenericRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GenericRepositoryImpl<T, ID extends Serializable> implements GenericRepository<T, ID> {

    protected final Class<T> entityType;

    public GenericRepositoryImpl(Class<T> entityType) {
        this.entityType = entityType;
    }

    protected void initLazyCollections(T entity) {
        // Hook for subclasses to initialize lazy collections
    }

    protected void beforeDelete(T entity) {
        // Hook for subclasses to run logic before deletion (e.g. dissociation)
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
            if (transaction != null) {
                transaction.rollback();
            }
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
            if (transaction != null) {
                transaction.rollback();
            }
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
            String hql = "from " + entityType.getSimpleName();
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
