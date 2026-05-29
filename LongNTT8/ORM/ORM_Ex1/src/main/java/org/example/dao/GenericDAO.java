package org.example.dao;

import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class GenericDAO<T> {

    private final Class<T> entityClass;

    public GenericDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected void executeInTransaction(Consumer<Session> action) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            action.accept(session);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    protected <R> R executeQueryInTransaction(Function<Session, R> action) {
        Transaction transaction = null;
        R result = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            result = action.apply(session);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return result;
    }

    public void save(T entity) {
        executeInTransaction(session -> session.persist(entity));
    }

    public void update(T entity) {
        executeInTransaction(session -> session.merge(entity));
    }

    public void delete(T entity) {
        executeInTransaction(session -> session.remove(session.contains(entity) ? entity : session.merge(entity)));
    }

    public Optional<T> findById(int id) {
        return executeQueryInTransaction(session -> Optional.ofNullable(session.get(entityClass, id)));
    }

    public List<T> findAll() {
        return executeQueryInTransaction(session ->
                session.createQuery("from " + entityClass.getName(), entityClass).list()
        );
    }
}
