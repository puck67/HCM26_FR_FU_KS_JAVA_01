package dao;

import entity.Course;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ultil.HibernateUtil;

import java.util.List;

public class CourseDao {

    // CREATE
    public Course save(Course course) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(course);
            tx.commit();
            return course;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    // UPDATE
    public Course update(Course course) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Course merged = (Course) session.merge(course);
            tx.commit();
            return merged;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    // DELETE
    public void delete(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Course course = session.get(Course.class, id);
            if (course != null) {
                // Remove course from all student associations before deleting
                course.getStudents().forEach(s -> s.getCourses().remove(course));
                session.remove(course);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    // GET BY ID
    public Course findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Course.class, id);
        }
    }

    // GET ALL
    public List<Course> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Course", Course.class).list();
        }
    }
}

