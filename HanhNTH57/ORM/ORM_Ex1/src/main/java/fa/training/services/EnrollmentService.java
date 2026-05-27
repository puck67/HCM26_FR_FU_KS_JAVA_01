package fa.training.services;

import fa.training.entities.Course;
import fa.training.entities.Student;
import fa.training.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.Set;

public class EnrollmentService {

    public void enroll(int studentId, int courseId) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            Student student = session.get(Student.class, studentId);
            Course course = session.get(Course.class, courseId);
            
            if (student != null && course != null) {
                student.addCourse(course);
                session.merge(student);
            }
            
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void unenroll(int studentId, int courseId) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            Student student = session.get(Student.class, studentId);
            Course course = session.get(Course.class, courseId);
            
            if (student != null && course != null) {
                student.removeCourse(course);
                session.merge(student);
            }
            
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public Set<Course> getStudentCourses(int studentId) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Student student = session.get(Student.class, studentId);
            if (student != null) {
                // Initialize the collection before the session closes
                student.getCourses().size(); 
                return student.getCourses();
            }
            return null;
        }
    }

    public Set<Student> getCourseStudents(int courseId) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Course course = session.get(Course.class, courseId);
            if (course != null) {
                // Initialize the collection before the session closes
                course.getStudents().size();
                return course.getStudents();
            }
            return null;
        }
    }
}
