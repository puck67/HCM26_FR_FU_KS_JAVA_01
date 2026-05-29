package fa.training.service;

import fa.training.dao.CourseDAO;
import fa.training.dao.StudentDAO;
import fa.training.dao.impl.CourseDAOImpl;
import fa.training.dao.impl.StudentDAOImpl;
import fa.training.entities.Course;
import fa.training.entities.Student;
import fa.training.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Set;

public class TrainingService {

    private final StudentDAO studentDAO = new StudentDAOImpl();
    private final CourseDAO courseDAO = new CourseDAOImpl();

    public void createStudent(Student student) {
        studentDAO.save(student);
    }

    public void updateStudent(Student student) {
        studentDAO.update(student);
    }

    public void deleteStudent(int id) {
        studentDAO.delete(id);
    }

    public Student getStudentById(int id) {
        return studentDAO.getById(id);
    }

    public List<Student> getAllStudents() {
        return studentDAO.getAll();
    }

    public List<Student> getAllStudents(int page, int pageSize) {
        return studentDAO.getAll(page, pageSize);
    }

    public void createCourse(Course course) {
        courseDAO.save(course);
    }

    public void updateCourse(Course course) {
        courseDAO.update(course);
    }

    public void deleteCourse(int id) {
        courseDAO.delete(id);
    }

    public Course getCourseById(int id) {
        return courseDAO.getById(id);
    }

    public List<Course> getAllCourses() {
        return courseDAO.getAll();
    }

    public void enrollStudentInCourse(int studentId, int courseId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Student student = session.get(Student.class, studentId);
            Course course = session.get(Course.class, courseId);
            if (student != null && course != null) {
                student.getCourses().add(course);
                session.update(student);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public void removeStudentFromCourse(int studentId, int courseId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Student student = session.get(Student.class, studentId);
            Course course = session.get(Course.class, courseId);
            if (student != null && course != null) {
                student.getCourses().remove(course);
                session.update(student);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public Set<Course> getCoursesOfStudent(int studentId) {
        Student student = studentDAO.getById(studentId);
        return student != null ? student.getCourses() : null;
    }

    public Set<Student> getStudentsOfCourse(int courseId) {
        Course course = courseDAO.getById(courseId);
        return course != null ? course.getStudents() : null;
    }

    public List<Student> findStudentsOlderThan(int age) {
        return studentDAO.findOlderThan(age);
    }

    public List<Object[]> findStudentsWithCourses() {
        return studentDAO.findStudentsWithCourses();
    }

    public List<Student> findStudentsByName(String name) {
        return studentDAO.findByName(name);
    }

    public List<Course> findCoursesByCreditGreaterThan(int credit) {
        return courseDAO.findByCreditGreaterThan(credit);
    }

    public List<Object[]> countStudentsPerCourse() {
        return courseDAO.countStudentsPerCourse();
    }

    public List<Student> findStudentsNotEnrolled() {
        return studentDAO.findStudentsNotEnrolled();
    }
}
