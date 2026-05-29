package org.example.dao;

import org.example.util.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Generic DAO cung cấp các thao tác CRUD cơ bản cho bất kỳ Entity nào.
 *
 * @param <T>  Kiểu của Entity (ví dụ: Student, Course)
 * @param <ID> Kiểu của khóa chính (ví dụ: Long, Integer)
 */
public abstract class GenericDAO<T, ID> {

    private final Class<T> entityClass;

    protected GenericDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }



    public T save(T entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            try {
                transaction = session.beginTransaction();
                T merged = session.merge(entity);
                transaction.commit();
                return merged;
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) transaction.rollback();
                throw e;
            }
        } catch (Exception e) {
            System.err.println("[" + entityClass.getSimpleName() + "] Error in save: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    //
    /**
     * Tìm một Entity theo ID.
     *
     * @param id Khóa chính của Entity
     * @return Entity tìm được, hoặc null nếu không tồn tại
     */
    public T findById(ID id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(entityClass, id);
        } catch (Exception e) {
            System.err.println("[" + entityClass.getSimpleName() + "] Error in findById: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Lấy toàn bộ danh sách Entity.
     *
     * @return Danh sách tất cả Entity
     */
    public List<T> findAll() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "FROM " + entityClass.getSimpleName();
            return session.createQuery(hql, entityClass).list();
        } catch (Exception e) {
            System.err.println("[" + entityClass.getSimpleName() + "] Error in findAll: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // =========================================================
    // UPDATE
    // =========================================================

    /**
     * Cập nhật thông tin của một Entity đã tồn tại.
     *
     * @param entity Entity cần cập nhật
     * @return Entity đã được cập nhật
     */
    public T update(T entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            try {
                transaction = session.beginTransaction();
                T merged = session.merge(entity);
                transaction.commit();
                return merged;
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) transaction.rollback();
                throw e;
            }
        } catch (Exception e) {
            System.err.println("[" + entityClass.getSimpleName() + "] Error in update: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // =========================================================
    // DELETE
    // =========================================================

    /**
     * Xóa một Entity theo ID.
     *
     * @param id Khóa chính của Entity cần xóa
     * @return true nếu xóa thành công, false nếu không tìm thấy hoặc lỗi
     */
    public boolean deleteById(ID id) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            try {
                transaction = session.beginTransaction();
                T entity = session.get(entityClass, id);
                if (entity != null) {
                    // Hook để subclass dọn dẹp quan hệ Many-to-Many trước khi xóa
                    beforeDelete(entity, session);
                    session.remove(entity);
                    transaction.commit();
                    return true;
                } else {
                    transaction.commit();
                    System.out.println("[" + entityClass.getSimpleName() + "] Entity with id=" + id + " not found.");
                    return false;
                }
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) transaction.rollback();
                throw e;
            }
        } catch (Exception e) {
            System.err.println("[" + entityClass.getSimpleName() + "] Error in deleteById: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Hook được gọi trước khi xóa entity.
     * Subclass override hàm này để dọn dẹp các liên kết (ví dụ: Many-to-Many)
     * nhằm tránh lỗi Foreign Key Constraint khi xóa.
     *
     * @param entity  Entity sắp bị xóa
     * @param session Session Hibernate hiện tại
     */
    protected void beforeDelete(T entity, Session session) {
        // Mặc định không làm gì, để subclass override nếu cần
    }
}
