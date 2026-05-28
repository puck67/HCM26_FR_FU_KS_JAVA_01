package com.example.service.impl;

import com.example.dao.CourseDAO;
import com.example.dao.StudentDAO;
import com.example.dao.impl.CourseDAOImpl;
import com.example.dao.impl.StudentDAOImpl;
import com.example.entity.Course;
import com.example.entity.Student;
import com.example.service.StudentService;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class StudentServiceImpl implements StudentService {

    private final StudentDAO studentDAO = new StudentDAOImpl();
    private final CourseDAO courseDAO = new CourseDAOImpl();

    @Override
    public Student createStudent(String name, int age) {
        // Validate input before hitting the database
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be empty.");
        }
        if (age <= 0 || age > 120) {
            throw new IllegalArgumentException("Age must be between 1 and 120.");
        }
        Student student = new Student(name.trim(), age);
        studentDAO.save(student);
        return student;
    }

    @Override
    public void updateStudent(int id, String name, int age) {
        Student existing = studentDAO.findById(id);
        if (existing == null) {
            throw new IllegalArgumentException("Student with ID " + id + " not found.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be empty.");
        }
        if (age <= 0 || age > 120) {
            throw new IllegalArgumentException("Age must be between 1 and 120.");
        }
        existing.setName(name.trim());
        existing.setAge(age);
        studentDAO.update(existing);
    }

    @Override
    public void deleteStudent(int id) {
        Student existing = studentDAO.findById(id);
        if (existing == null) {
            throw new IllegalArgumentException("Student with ID " + id + " not found.");
        }
        studentDAO.delete(id);
    }

    @Override
    public Student getStudentById(int id) {
        Student student = studentDAO.findById(id);
        if (student == null) {
            throw new IllegalArgumentException("Student with ID " + id + " not found.");
        }
        return student;
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

            Student s = session.get(Student.class, studentId);
            Course c = session.get(Course.class, courseId);

            if (s == null) throw new IllegalArgumentException("Student ID " + studentId + " not found.");
            if (c == null) throw new IllegalArgumentException("Course ID " + courseId + " not found.");

            boolean alreadyEnrolled = s.getCourses().stream()
                    .anyMatch(course -> course.getId() == courseId);
            if (alreadyEnrolled) {
                throw new IllegalStateException("Student is already enrolled in this course.");
            }

            s.addCourse(c);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        }
    }

    @Override
    public void removeStudentFromCourse(int studentId, int courseId) {
        Student student = studentDAO.findById(studentId);
        Course course = courseDAO.findById(courseId);

        if (student == null) throw new IllegalArgumentException("Student ID " + studentId + " not found.");
        if (course == null) throw new IllegalArgumentException("Course ID " + courseId + " not found.");

        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Student s = session.get(Student.class, studentId);
            Course c = session.get(Course.class, courseId);
            s.removeCourse(c);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        }
    }

    @Override
    public List<Course> getCoursesOfStudent(int studentId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Student student = session.get(Student.class, studentId);
            if (student == null) throw new IllegalArgumentException("Student ID " + studentId + " not found.");
            student.getCourses().size();
            return new ArrayList<>(student.getCourses());
        }
    }

    @Override
    public List<Student> findStudentsOlderThan(int age) {
        return studentDAO.findOlderThan(age);
    }

    @Override
    public List<Student> findStudentsWithCourses() {
        return studentDAO.findWithCourses();
    }

    @Override
    public List<Student> findStudentsByName(String name) {
        return studentDAO.findByName(name);
    }

    @Override
    public List<Student> findStudentsByCourse(int courseId) {
        Course course = courseDAO.findById(courseId);
        if (course == null) throw new IllegalArgumentException("Course ID " + courseId + " not found.");
        return studentDAO.findByCourseId(courseId);
    }
}