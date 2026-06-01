package com.example.dao.impl;

import com.example.dao.CourseDAO;
import com.example.entity.Course;
import com.example.util.HibernateUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
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
                // Remove course from all associated students' sets manually to clean up references in session
                course.getStudents().forEach(student -> student.getCourses().remove(course));
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
            String hql = "SELECT DISTINCT c FROM Course c LEFT JOIN FETCH c.students WHERE c.id = :id";
            Query<Course> query = session.createQuery(hql, Course.class);
            query.setParameter("id", id);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Course> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT DISTINCT c FROM Course c LEFT JOIN FETCH c.students", Course.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Course> findCoursesByCreditGreaterThan(int credit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Course> cr = cb.createQuery(Course.class);
            Root<Course> root = cr.from(Course.class);
            // Fetch students eagerly to avoid lazy init exception when displaying results
            root.fetch("students", jakarta.persistence.criteria.JoinType.LEFT);
            cr.select(root).distinct(true).where(cb.gt(root.get("credit"), credit));
            return session.createQuery(cr).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Object[]> countStudentsPerCourse() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Aggregation query: count students enrolled in each course
            String hql = "SELECT c.title, COUNT(s.id) FROM Course c LEFT JOIN c.students s GROUP BY c.title";
            return session.createQuery(hql, Object[].class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
