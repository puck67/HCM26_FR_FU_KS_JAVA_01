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

public class CourseDAOImpl implements CourseDAO {

    @Override
    public void save(Course course) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(course);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void update(Course course) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(course);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int courseId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Course course = session.get(Course.class, courseId);
            if (course != null) {
                session.remove(course);
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
    public Course findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Course.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Course> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Course", Course.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Course> findCoursesWithCreditGreaterThan(int credit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Course> cr = cb.createQuery(Course.class);
            Root<Course> root = cr.from(Course.class);
            cr.select(root).where(cb.greaterThan(root.get("credit"), credit));
            return session.createQuery(cr).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Object[]> countStudentsPerCourse() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "select c.title, count(s.id) from Course c left join c.students s group by c.id, c.title",
                    Object[].class
            ).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
