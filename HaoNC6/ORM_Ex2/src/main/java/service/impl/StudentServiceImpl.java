package service.impl;

import dao.CourseDAO;
import dao.StudentDAO;
import dao.impl.CourseDAOImpl;
import dao.impl.StudentDAOImpl;
import entity.Course;
import entity.Student;
import service.StudentService;
import util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StudentServiceImpl implements StudentService {

    private final StudentDAO studentDAO = new StudentDAOImpl();
    private final CourseDAO courseDAO = new CourseDAOImpl();

    @Override
    public void createStudent(String name, int age) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be empty.");
        }
        if (age <= 0) {
            throw new IllegalArgumentException("Student age must be positive.");
        }
        studentDAO.save(new Student(name, age));
    }

    @Override
    public void updateStudent(int id, String name, int age) {
        Student student = studentDAO.findById(id);
        if (student == null) {
            throw new IllegalArgumentException("Student with ID " + id + " not found.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be empty.");
        }
        if (age <= 0) {
            throw new IllegalArgumentException("Student age must be positive.");
        }
        student.setName(name);
        student.setAge(age);
        studentDAO.update(student);
    }

    @Override
    public void deleteStudent(int id) {
        if (studentDAO.findById(id) == null) {
            throw new IllegalArgumentException("Student with ID " + id + " not found.");
        }
        studentDAO.delete(id);
    }

    @Override
    public Student getStudentById(int id) {
        return studentDAO.findById(id);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentDAO.findAll();
    }

    @Override
    public void enrollStudentInCourse(int studentId, int courseId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Student student = session.get(Student.class, studentId);
            Course course = session.get(Course.class, courseId);
            if (student == null) {
                throw new IllegalArgumentException("Student not found.");
            }
            if (course == null) {
                throw new IllegalArgumentException("Course not found.");
            }
            student.enroll(course);
            session.merge(student);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public void removeStudentFromCourse(int studentId, int courseId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Student student = session.get(Student.class, studentId);
            Course course = session.get(Course.class, courseId);
            if (student == null) {
                throw new IllegalArgumentException("Student not found.");
            }
            if (course == null) {
                throw new IllegalArgumentException("Course not found.");
            }
            student.unenroll(course);
            session.merge(student);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public Set<Course> getCoursesOfStudent(int studentId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Student student = session.get(Student.class, studentId);
            if (student != null) {
                // Initialize Lazy Collection
                org.hibernate.Hibernate.initialize(student.getCourses());
                return student.getCourses();
            }
            return new HashSet<>();
        }
    }

    // ========== Queries ==========

    @Override
    public List<Student> findStudentsOlderThan(int age) {
        return studentDAO.findOlderThan(age);
    }

    @Override
    public List<Object[]> findStudentsWithCourses() {
        return studentDAO.findStudentsWithCourses();
    }

    @Override
    public List<Student> findStudentsByName(String name) {
        return studentDAO.findByName(name);
    }

    @Override
    public List<Object[]> countStudentsPerCourse() {
        return studentDAO.countStudentsPerCourse();
    }

    @Override
    public List<Student> findStudentsEnrolledInCourse(int courseId) {
        return studentDAO.findStudentsEnrolledInCourse(courseId);
    }
}
