package com.example.service.impl;

import com.example.dao.StudentDAO;
import com.example.dao.impl.StudentDAOImpl;
import com.example.entity.Course;
import com.example.entity.Student;
import com.example.service.StudentService;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Set;

public class StudentServiceImpl implements StudentService {

    private final StudentDAO studentDAO = new StudentDAOImpl();

    @Override
    public Student createStudent(String name, int age) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be empty");
        }
        if (!name.matches("^[\\p{L}\\s']+$")) {
            throw new IllegalArgumentException("Student name is invalid (numbers or special characters not allowed)");
        }
        if (age < 6 || age > 100) {
            throw new IllegalArgumentException("Student age must be between 6 and 100");
        }
        Student student = new Student(name, age);
        studentDAO.save(student);
        return student;
    }

    @Override
    public void updateStudent(int id, String name, int age) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be empty");
        }
        if (!name.matches("^[\\p{L}\\s']+$")) {
            throw new IllegalArgumentException("Student name is invalid (numbers or special characters not allowed)");
        }
        if (age < 6 || age > 100) {
            throw new IllegalArgumentException("Student age must be between 6 and 100");
        }
        Student student = studentDAO.findById(id);
        if (student == null) {
            throw new RuntimeException("Student with ID " + id + " does not exist");
        }
        student.setName(name);
        student.setAge(age);
        studentDAO.update(student);
    }

    @Override
    public void deleteStudent(int id) {
        Student student = studentDAO.findById(id);
        if (student == null) {
            throw new RuntimeException("Student with ID " + id + " does not exist");
        }
        studentDAO.delete(id);
    }

    @Override
    public Student getStudentById(int id) {
        Student student = studentDAO.findById(id);
        if (student == null) {
            throw new RuntimeException("Student with ID " + id + " does not exist");
        }
        return student;
    }

    @Override
    public List<Student> getAllStudents() {
        return studentDAO.findAll();
    }

    @Override
    public void enrollStudentInCourse(int studentId, int courseId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Student student = session.get(Student.class, studentId);
            Course course = session.get(Course.class, courseId);
            if (student == null) {
                throw new RuntimeException("Student with ID " + studentId + " does not exist");
            }
            if (course == null) {
                throw new RuntimeException("Course with ID " + courseId + " does not exist");
            }
            if (student.getCourses().contains(course)) {
                throw new RuntimeException("Student is already enrolled in this course");
            }
            student.addCourse(course);
            session.merge(student);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(e.getMessage(), e);
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
                throw new RuntimeException("Student with ID " + studentId + " does not exist");
            }
            if (course == null) {
                throw new RuntimeException("Course with ID " + courseId + " does not exist");
            }
            if (!student.getCourses().contains(course)) {
                throw new RuntimeException("Student is not enrolled in this course");
            }
            student.removeCourse(course);
            session.merge(student);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Set<Course> getCoursesOfStudent(int studentId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Student student = session.createQuery(
                "select s from Student s left join fetch s.courses where s.id = :id", Student.class)
                .setParameter("id", studentId)
                .uniqueResult();
            if (student == null) {
                throw new RuntimeException("Student with ID " + studentId + " does not exist");
            }
            return student.getCourses();
        }
    }

    @Override
    public List<Student> findStudentsOlderThan(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Age threshold cannot be negative");
        }
        return studentDAO.findOlderThan(age);
    }

    @Override
    public List<Student> findStudentsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Search name cannot be empty");
        }
        return studentDAO.findByName(name);
    }

    @Override
    public List<Student> getStudentsWithCourses() {
        return studentDAO.findAllWithCourses();
    }

    @Override
    public List<Student> getStudentsEnrolledInCourse(int courseId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Course course = session.get(Course.class, courseId);
            if (course == null) {
                throw new RuntimeException("Course with ID " + courseId + " does not exist");
            }
        }
        return studentDAO.findStudentsByCourseId(courseId);
    }
}
