package fa.training.dao.impl;

import fa.training.dao.StudentDAO;
import fa.training.entities.Course;
import fa.training.entities.Student;
import fa.training.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StudentDAOImpl extends GenericDAOImpl<Student, Integer> implements StudentDAO {

    private static final Logger logger = LoggerFactory.getLogger(StudentDAOImpl.class);

    public StudentDAOImpl() {
        super(Student.class);
    }

    @Override
    protected void cleanupBeforeDelete(Session session, Student student) {
        student.getCourses().clear();
    }

    @Override
    public List<Student> findAllPaginated(int pageNumber, int pageSize) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Student s ORDER BY s.id", Student.class)
                    .setFirstResult((pageNumber - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .list();
        }
    }

    @Override
    public void enrollStudentInCourse(int studentId, int courseId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Student student = session.get(Student.class, studentId);
            Course course = session.get(Course.class, courseId);
            if (student == null) {
                throw new IllegalArgumentException("Student with ID " + studentId + " does not exist.");
            }
            if (course == null) {
                throw new IllegalArgumentException("Course with ID " + courseId + " does not exist.");
            }
            student.addCourse(course);
            session.merge(student);
            transaction.commit();
        } catch (HibernateException | IllegalArgumentException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error enrolling student ID {} in course ID {}: {}", studentId, courseId, e.getMessage());
            throw e;
        }
    }

    @Override
    public void removeStudentFromCourse(int studentId, int courseId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Student student = session.get(Student.class, studentId);
            Course course = session.get(Course.class, courseId);
            if (student == null) {
                throw new IllegalArgumentException("Student with ID " + studentId + " does not exist.");
            }
            if (course == null) {
                throw new IllegalArgumentException("Course with ID " + courseId + " does not exist.");
            }
            student.removeCourse(course);
            session.merge(student);
            transaction.commit();
        } catch (HibernateException | IllegalArgumentException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error removing student ID {} from course ID {}: {}", studentId, courseId, e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Course> getCoursesByStudent(int studentId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Student student = session.createQuery(
                    "SELECT s FROM Student s LEFT JOIN FETCH s.courses WHERE s.id = :id", Student.class)
                    .setParameter("id", studentId)
                    .uniqueResult();
            return student != null ? new ArrayList<>(student.getCourses()) : Collections.emptyList();
        }
    }

    @Override
    public List<Student> findStudentsOlderThan(int age) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Student s WHERE s.age > :age", Student.class)
                    .setParameter("age", age)
                    .list();
        }
    }

    @Override
    public List<Object[]> listStudentsAndCourses() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "SELECT s.name, c.title FROM Student s JOIN s.courses c", Object[].class)
                    .list();
        }
    }

    @Override
    public List<Student> findByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNamedQuery("Student.findByName", Student.class)
                    .setParameter("name", name)
                    .list();
        }
    }

    @Override
    public List<Student> findStudentsNotEnrolled() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "SELECT s FROM Student s WHERE s.courses IS EMPTY", Student.class)
                    .list();
        }
    }
}
