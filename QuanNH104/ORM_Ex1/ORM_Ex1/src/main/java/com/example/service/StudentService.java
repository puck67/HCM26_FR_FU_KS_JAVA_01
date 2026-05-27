package com.example.service;

import com.example.dao.StudentDAO;
import com.example.dao.CourseDAO;
import com.example.model.Course;
import com.example.model.Student;
import com.example.validation.Validator;

import java.util.List;

public class StudentService {
    private final StudentDAO studentDAO = new StudentDAO();
    private final CourseDAO courseDAO = new CourseDAO();

    public void saveStudent(Student student) {
        Validator.validateStudent(student.getName(), student.getAge());
        studentDAO.saveStudent(student);
    }

    public void updateStudent(Student student) {
        Student existing = studentDAO.getStudent(student.getId());
        if (existing == null) {
            throw new IllegalArgumentException("Student not found with ID " + student.getId());
        }
        Validator.validateStudent(student.getName(), student.getAge());
        studentDAO.updateStudent(student);
    }

    public void deleteStudent(int id) {
        Student existing = studentDAO.getStudent(id);
        if (existing == null) {
            throw new IllegalArgumentException("Student not found with ID " + id);
        }
        studentDAO.deleteStudent(id);
    }

    public Student getStudent(int id) {
        Student student = studentDAO.getStudent(id);
        if (student == null) {
            throw new IllegalArgumentException("Student not found with ID " + id);
        }
        return student;
    }

    public List<Student> getAllStudents() {
        return studentDAO.getAllStudents();
    }

    public void enrollStudentInCourse(int studentId, int courseId) {
        Student student = studentDAO.getStudent(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Student with ID " + studentId + " does not exist!");
        }

        Course course = courseDAO.getCourse(courseId);
        if (course == null) {
            throw new IllegalArgumentException("Course with ID " + courseId + " does not exist!");
        }

        List<Course> enrolledCourses = studentDAO.getCoursesOfStudent(studentId);
        boolean alreadyEnrolled = enrolledCourses.stream().anyMatch(c -> c.getId() == courseId);
        if (alreadyEnrolled) {
            throw new IllegalArgumentException("Student is already enrolled in this course!");
        }

        studentDAO.enrollStudentInCourse(studentId, courseId);
    }

    public void removeStudentFromCourse(int studentId, int courseId) {
        Student student = studentDAO.getStudent(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Student with ID " + studentId + " does not exist!");
        }

        Course course = courseDAO.getCourse(courseId);
        if (course == null) {
            throw new IllegalArgumentException("Course with ID " + courseId + " does not exist!");
        }

        List<Course> enrolledCourses = studentDAO.getCoursesOfStudent(studentId);
        boolean isEnrolled = enrolledCourses.stream().anyMatch(c -> c.getId() == courseId);
        if (!isEnrolled) {
            throw new IllegalArgumentException("Student is not enrolled in this course!");
        }

        studentDAO.removeStudentFromCourse(studentId, courseId);
    }

    public List<Course> getCoursesOfStudent(int studentId) {
        return studentDAO.getCoursesOfStudent(studentId);
    }
}
