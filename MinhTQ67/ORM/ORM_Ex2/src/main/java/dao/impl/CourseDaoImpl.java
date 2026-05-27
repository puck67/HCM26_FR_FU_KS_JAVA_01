package dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dao.CourseDao;
import entity.Course;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public class CourseDaoImpl implements CourseDao {

    private static final Logger logger = LoggerFactory.getLogger(CourseDaoImpl.class);

    @Override
    public void save(Course course) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(course);
            tx.commit();
            logger.info("Course saved: {}", course);
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            logger.error("Error saving course: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void update(Course course) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(course);
            tx.commit();
            logger.info("Course updated: {}", course);
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            logger.error("Error updating course: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void delete(int courseId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Course course = session.get(Course.class, courseId);
            if (course != null) {
                // Remove from all students first
                for (entity.Student student : course.getStudents()) {
                    student.getCourses().remove(course);
                }
                course.getStudents().clear();
                session.remove(course);
                logger.info("Course deleted with id: {}", courseId);
            } else {
                logger.warn("Course not found with id: {}", courseId);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            logger.error("Error deleting course: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Course findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Course.class, id);
        }
    }

    @Override
    public List<Course> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Course", Course.class).list();
        }
    }
}
