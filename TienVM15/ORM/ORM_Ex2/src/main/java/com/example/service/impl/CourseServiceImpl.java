package com.example.service.impl;

import com.example.dao.CourseDAO;
import com.example.dao.StudentDAO;
import com.example.dao.impl.CourseDAOImpl;
import com.example.dao.impl.StudentDAOImpl;
import com.example.entity.Course;
import com.example.entity.Student;
import com.example.service.CourseService;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * CourseServiceImpl delegates course CRUD to CourseDAO and handles
 * enrollment (session-scoped operations) directly via Hibernate session.
 */
public class CourseServiceImpl implements CourseService {

    private final CourseDAO courseDAO = new CourseDAOImpl();
    private final StudentDAO studentDAO = new StudentDAOImpl();

    @Override
    public void createCourse(String title, int credit) {
        courseDAO.save(new Course(title, credit));
    }

    @Override
    public boolean updateCourse(int id, String title, int credit) {
        Course course = courseDAO.findById(id);
        if (course != null) {
            course.setTitle(title);
            course.setCredit(credit);
            courseDAO.update(course);
            return true;
        }
        System.out.println("Course with ID " + id + " not found.");
        return false;
    }

    @Override
    public boolean deleteCourse(int id) {
        Course course = courseDAO.findById(id);
        if (course != null) {
            courseDAO.delete(id);
            return true;
        }
        System.out.println("Course with ID " + id + " not found.");
        return false;
    }

    @Override
    public Course getCourse(int id) {
        return courseDAO.findById(id);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseDAO.findAll();
    }

    @Override
    public List<Course> getCoursesWithCreditGreaterThan(int credit) {
        return courseDAO.findWithCreditGreaterThan(credit);
    }

    @Override
    public Map<String, Long> getStudentCountPerCourse() {
        return courseDAO.getStudentCountPerCourse();
    }

    /** Enroll a student into a course within a single transaction */
    @Override
    public void enrollStudent(int studentId, int courseId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Student student = session.get(Student.class, studentId);
            Course course = session.get(Course.class, courseId);
            if (student != null && course != null) {
                student.addCourse(course);
                session.merge(student);
            } else {
                System.out.println("Enrollment failed: Student or Course not found.");
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /** Remove a student from a course within a single transaction */
    @Override
    public void removeStudentFromCourse(int studentId, int courseId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Student student = session.get(Student.class, studentId);
            Course course = session.get(Course.class, courseId);
            if (student != null && course != null) {
                student.removeCourse(course);
                session.merge(student);
            } else {
                System.out.println("Disenrollment failed: Student or Course not found.");
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public List<Course> getCoursesOfStudent(int studentId) {
        Student student = studentDAO.findById(studentId);
        if (student != null) {
            return new ArrayList<>(student.getCourses());
        }
        return new ArrayList<>();
    }

    @Override
    public List<Student> getStudentsOfCourse(int courseId) {
        Course course = courseDAO.findById(courseId);
        if (course != null) {
            return new ArrayList<>(course.getStudents());
        }
        return new ArrayList<>();
    }
}
