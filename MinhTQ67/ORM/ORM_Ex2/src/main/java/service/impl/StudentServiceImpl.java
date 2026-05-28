package service.impl;

import dao.CourseDao;
import dao.StudentDao;
import dao.impl.CourseDaoImpl;
import dao.impl.StudentDaoImpl;
import entity.Course;
import entity.Student;
import org.hibernate.Session;
import org.hibernate.Transaction;
import service.StudentService;
import util.HibernateUtil;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StudentServiceImpl implements StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);
    private final StudentDao studentDAO = new StudentDaoImpl();
    private final CourseDao courseDAO = new CourseDaoImpl();

    @Override
    public void createStudent(String name, int age) {
        Student student = new Student(name, age);
        studentDAO.save(student);
    }

    @Override
    public void updateStudent(int id, String name, int age) {
        Student student = studentDAO.findById(id);
        if (student == null) throw new RuntimeException("Student not found with id: " + id);
        student.setName(name);
        student.setAge(age);
        studentDAO.update(student);
    }

    @Override
    public void deleteStudent(int id) {
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
            if (student == null) throw new RuntimeException("Student not found with id: " + studentId);
            if (course == null) throw new RuntimeException("Course not found with id: " + courseId);
            student.getCourses().add(course);
            course.getStudents().add(student);
            session.merge(student);
            tx.commit();
            logger.info("Student {} enrolled in course {}", studentId, courseId);
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
            if (student == null) throw new RuntimeException("Student not found with id: " + studentId);
            if (course == null) throw new RuntimeException("Course not found with id: " + courseId);
            student.getCourses().remove(course);
            course.getStudents().remove(student);
            session.merge(student);
            tx.commit();
            logger.info("Student {} removed from course {}", studentId, courseId);
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public List<Course> getCoursesOfStudent(int studentId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Student student = session.get(Student.class, studentId);
            if (student == null) throw new RuntimeException("Student not found with id: " + studentId);
            return new ArrayList<>(student.getCourses());
        }
    }

    // ===== Advanced Queries =====

    @Override
    public List<Student> findStudentsOlderThan(int age) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Student s WHERE s.age > :age", Student.class)
                    .setParameter("age", age)
                    .list();
        }
    }

    @Override
    public List<Object[]> findStudentsWithCourses() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT s.name, c.title FROM Student s JOIN s.courses c", Object[].class)
                    .list();
        }
    }

    @Override
    public List<Student> findStudentsByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNamedQuery("Student.findByName", Student.class)
                    .setParameter("name", "%" + name + "%")
                    .list();
        }
    }

    @Override
    public List<Student> findStudentsEnrolledInCourse(int courseId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT s FROM Student s JOIN s.courses c WHERE c.id = :courseId", Student.class)
                    .setParameter("courseId", courseId)
                    .list();
        }
    }
}
