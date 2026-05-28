package com.example.service.impl;

import com.example.dao.CourseDAO;
import com.example.dao.StudentDAO;
import com.example.dao.impl.CourseDAOImpl;
import com.example.dao.impl.StudentDAOImpl;
import com.example.entity.Course;
import com.example.entity.Student;
import com.example.service.StudentService;

import java.util.List;
import java.util.Set;

public class StudentServiceImpl implements StudentService {

    private final StudentDAO studentDAO = new StudentDAOImpl();
    private final CourseDAO courseDAO = new CourseDAOImpl();

    @Override
    public Student createStudent(String name, int age) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be empty.");
        }
        if (age < 0) {
            throw new IllegalArgumentException("Student age cannot be negative.");
        }
        Student student = new Student(name.trim(), age);
        studentDAO.save(student);
        return student;
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
        if (age < 0) {
            throw new IllegalArgumentException("Student age cannot be negative.");
        }
        student.setName(name.trim());
        student.setAge(age);
        studentDAO.update(student);
    }

    @Override
    public void deleteStudent(int id) {
        Student student = studentDAO.findById(id);
        if (student == null) {
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
        Student student = studentDAO.findById(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Student with ID " + studentId + " not found.");
        }
        Course course = courseDAO.findById(courseId);
        if (course == null) {
            throw new IllegalArgumentException("Course with ID " + courseId + " not found.");
        }

        if (student.getCourses().contains(course)) {
            throw new IllegalStateException("Student is already enrolled in this course.");
        }

        student.addCourse(course);
        studentDAO.update(student);
    }

    @Override
    public void removeStudentFromCourse(int studentId, int courseId) {
        Student student = studentDAO.findById(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Student with ID " + studentId + " not found.");
        }
        Course course = courseDAO.findById(courseId);
        if (course == null) {
            throw new IllegalArgumentException("Course with ID " + courseId + " not found.");
        }

        if (!student.getCourses().contains(course)) {
            throw new IllegalStateException("Student is not enrolled in this course.");
        }

        student.removeCourse(course);
        studentDAO.update(student);
    }

    @Override
    public Set<Course> getCoursesOfStudent(int studentId) {
        Student student = studentDAO.findById(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Student with ID " + studentId + " not found.");
        }
        return student.getCourses();
    }

    @Override
    public List<Student> findStudentsOlderThan(int age) {
        return studentDAO.findByAgeGreaterThan(age);
    }

    @Override
    public List<Student> findStudentsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Student name query cannot be empty.");
        }
        return studentDAO.findByNameNamedQuery(name.trim());
    }

    @Override
    public List<Object[]> getStudentsAndCourseTitles() {
        return studentDAO.findAllStudentsWithCourseTitles();
    }

    @Override
    public List<Student> findStudentsEnrolledInCourse(int courseId) {
        Course course = courseDAO.findById(courseId);
        if (course == null) {
            throw new IllegalArgumentException("Course with ID " + courseId + " not found.");
        }
        return studentDAO.findStudentsByCourseId(courseId);
    }
}
