package com.example.dao.impl;

import com.example.dao.CourseDAO;
import com.example.entity.Course;
import com.example.util.HibernateUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class CourseDAOImpl implements CourseDAO {

    @Override
    public void save(Course course) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(course);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Lưu course thất bại: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Course course) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(course);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Cập nhật course thất bại: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int courseId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Course managed = session.get(Course.class, courseId);
            if (managed != null) {
                managed.getStudents().forEach(s -> s.getCourses().remove(managed));
                managed.getStudents().clear();
                session.remove(managed);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Xóa course thất bại: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Course> findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Course.class, id));
        }
    }

    @Override
    public List<Course> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Course", Course.class).list();
        }
    }

    @Override
    public List<Course> findByMinCredit(int minCredit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb    = session.getCriteriaBuilder();
            CriteriaQuery<Course> cq   = cb.createQuery(Course.class);
            Root<Course> root          = cq.from(Course.class);
            cq.select(root).where(cb.gt(root.get("credit"), minCredit));
            return session.createQuery(cq).list();
        }
    }
}
