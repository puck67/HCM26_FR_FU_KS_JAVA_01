package dao;

import entity.Course;
import entity.Student;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;
import java.util.Set;

/**
 * CourseDAO - CRUD and query operations for Course entity.
 */
public class CourseDAO {

    /**
     * Create a new course
     */
    public void create(Course course) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(course);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    /**
     * Update course information
     */
    public void update(Course course) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(course);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    /**
     * Delete a course by ID
     */
    public void delete(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Course course = session.get(Course.class, id);
            if (course != null) {
                for (Student s : course.getStudents()) {
                    s.getCourses().remove(course);
                }
                course.getStudents().clear();
                session.remove(course);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    /**
     * Get a course by ID
     */
    public Course getById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Course.class, id);
        }
    }

    /**
     * Get all courses
     */
    public List<Course> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Course", Course.class).list();
        }
    }

    /**
     * Get all students of a course
     */
    public Set<Student> getStudentsOfCourse(int courseId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Course course = session.get(Course.class, courseId);
            if (course != null) {
                org.hibernate.Hibernate.initialize(course.getStudents());
                return course.getStudents();
            }
            return new java.util.HashSet<>();
        }
    }

    /**
     * Criteria API: Find courses with credit greater than given value.
     */
    public List<Course> findByMinCredit(int minCredit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Course> cq = cb.createQuery(Course.class);
            Root<Course> root = cq.from(Course.class);
            cq.select(root).where(cb.gt(root.get("credit"), minCredit));
            return session.createQuery(cq).list();
        }
    }
}