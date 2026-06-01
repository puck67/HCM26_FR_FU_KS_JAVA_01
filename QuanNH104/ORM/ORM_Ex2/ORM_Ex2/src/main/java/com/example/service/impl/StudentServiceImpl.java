package com.example.service.impl;

import com.example.dao.CourseDAO;
import com.example.dao.StudentDAO;
import com.example.dao.impl.CourseDAOImpl;
import com.example.dao.impl.StudentDAOImpl;
import com.example.entity.Course;
import com.example.entity.Student;
import com.example.service.StudentService;
import com.example.validation.Validator;
import java.util.ArrayList;
import java.util.List;

public class StudentServiceImpl implements StudentService {

    private final StudentDAO studentDAO = new StudentDAOImpl();
    private final CourseDAO courseDAO = new CourseDAOImpl();

    @Override
    public void saveStudent(Student student) {
        Validator.validateStudent(student.getName(), student.getAge());
        studentDAO.save(student);
    }

    @Override
    public void updateStudent(Student student) {
        Student existing = studentDAO.findById(student.getId());
        if (existing == null) {
            throw new IllegalArgumentException("Student not found with ID " + student.getId());
        }
        Validator.validateStudent(student.getName(), student.getAge());
        studentDAO.update(student);
    }

    @Override
    public void deleteStudent(int id) {
        Student existing = studentDAO.findById(id);
        if (existing == null) {
            throw new IllegalArgumentException("Student not found with ID " + id);
        }
        studentDAO.delete(id);
    }

    @Override
    public Student getStudent(int id) {
        Student student = studentDAO.findById(id);
        if (student == null) {
            throw new IllegalArgumentException("Student not found with ID " + id);
        }
        return student;
    }

    @Override
    public List<Student> getAllStudents() {
        return studentDAO.findAll();
    }

    @Override
    public void enrollStudentInCourse(int studentId, int courseId) {
        Student student = studentDAO.findById(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Student with ID " + studentId + " does not exist!");
        }

        Course course = courseDAO.findById(courseId);
        if (course == null) {
            throw new IllegalArgumentException("Course with ID " + courseId + " does not exist!");
        }

        List<Course> enrolledCourses = getCoursesOfStudent(studentId);
        boolean alreadyEnrolled = enrolledCourses.stream().anyMatch(c -> c.getId() == courseId);
        if (alreadyEnrolled) {
            throw new IllegalArgumentException("Student is already enrolled in this course!");
        }

        student.addCourse(course);
        studentDAO.update(student);
    }

    @Override
    public void removeStudentFromCourse(int studentId, int courseId) {
        Student student = studentDAO.findById(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Student with ID " + studentId + " does not exist!");
        }

        Course course = courseDAO.findById(courseId);
        if (course == null) {
            throw new IllegalArgumentException("Course with ID " + courseId + " does not exist!");
        }

        List<Course> enrolledCourses = getCoursesOfStudent(studentId);
        boolean isEnrolled = enrolledCourses.stream().anyMatch(c -> c.getId() == courseId);
        if (!isEnrolled) {
            throw new IllegalArgumentException("Student is not enrolled in this course!");
        }

        student.removeCourse(course);
        studentDAO.update(student);
    }

    @Override
    public List<Course> getCoursesOfStudent(int studentId) {
        Student student = studentDAO.findById(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Student with ID " + studentId + " does not exist!");
        }
        return new ArrayList<>(student.getCourses());
    }

    @Override
    public List<Student> findStudentsOlderThan(int age) {
        return studentDAO.findByAgeGreaterThan(age);
    }

    @Override
    public List<Student> findStudentsByName(String name) {
        return studentDAO.findByNameNamedQuery(name);
    }

    @Override
    public List<Object[]> getStudentsAndTheirCourses() {
        return studentDAO.findAllStudentsWithCourses();
    }

    @Override
    public List<Student> getStudentsByCourseId(int courseId) {
        return studentDAO.findStudentsByCourseId(courseId);
    }
}
