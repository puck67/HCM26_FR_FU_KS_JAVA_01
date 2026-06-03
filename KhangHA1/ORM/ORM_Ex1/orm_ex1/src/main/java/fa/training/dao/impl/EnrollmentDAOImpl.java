package fa.training.dao.impl;

import fa.training.dao.EnrollmentDAO;
import fa.training.entity.Course;
import fa.training.entity.Student;
import fa.training.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Hibernate implementation of EnrollmentDAO.
 */
public class EnrollmentDAOImpl implements EnrollmentDAO {

    @Override
    public boolean enroll(int studentId, int courseId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Student student = session.get(Student.class, studentId);
            Course  course  = session.get(Course.class,  courseId);
            if (student == null || course == null) {
                tx.rollback();
                return false;
            }
            student.enrollIn(course);
            session.merge(student);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("[EnrollmentDAOImpl] Error enrolling: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean unenroll(int studentId, int courseId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Student student = session.get(Student.class, studentId);
            Course  course  = session.get(Course.class,  courseId);
            if (student == null || course == null) {
                tx.rollback();
                return false;
            }
            student.dropCourse(course);
            session.merge(student);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("[EnrollmentDAOImpl] Error unenrolling: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Set<Course> getCoursesOfStudent(int studentId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Student student = session.get(Student.class, studentId);
            if (student == null) return new HashSet<>();
            return new HashSet<>(student.getCourses());
        } catch (Exception e) {
            System.err.println("[EnrollmentDAOImpl] Error getting courses: " + e.getMessage());
            return new HashSet<>();
        }
    }

    @Override
    public Set<Student> getStudentsOfCourse(int courseId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Course course = session.get(Course.class, courseId);
            if (course == null) return new HashSet<>();
            return new HashSet<>(course.getStudents());
        } catch (Exception e) {
            System.err.println("[EnrollmentDAOImpl] Error getting students: " + e.getMessage());
            return new HashSet<>();
        }
    }

    @Override
    public List<Student> findStudentsNotEnrolled() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Student s WHERE s.courses IS EMPTY", Student.class
            ).list();
        } catch (Exception e) {
            System.err.println("[EnrollmentDAOImpl] Error: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
