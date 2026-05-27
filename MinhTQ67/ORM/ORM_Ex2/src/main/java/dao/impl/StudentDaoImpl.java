package dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dao.StudentDao;
import entity.Student;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public class StudentDaoImpl implements StudentDao {

    private static final Logger logger = LoggerFactory.getLogger(StudentDaoImpl.class);

    @Override
    public void save(Student student) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(student);
            tx.commit();
            logger.info("Student saved: {}", student);
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            logger.error("Error saving student: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void update(Student student) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(student);
            tx.commit();
            logger.info("Student updated: {}", student);
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            logger.error("Error updating student: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void delete(int studentId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Student student = session.get(Student.class, studentId);
            if (student != null) {
                // Remove from all courses first to clean join table
                for (entity.Course course : student.getCourses()) {
                    course.getStudents().remove(student);
                }
                student.getCourses().clear();
                session.remove(student);
                logger.info("Student deleted with id: {}", studentId);
            } else {
                logger.warn("Student not found with id: {}", studentId);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            logger.error("Error deleting student: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Student findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Student.class, id);
        }
    }

    @Override
    public List<Student> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Student", Student.class).list();
        }
    }
}
