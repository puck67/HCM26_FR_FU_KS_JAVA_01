package com.example.service.impl;

import com.example.dao.CourseDAO;
import com.example.dao.StudentDAO;
import com.example.dao.impl.CourseDAOImpl;
import com.example.dao.impl.StudentDAOImpl;
import com.example.entity.Course;
import com.example.entity.Student;
import com.example.service.StudentService;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class StudentServiceImpl implements StudentService {

    private final StudentDAO studentDAO = new StudentDAOImpl();
    private final CourseDAO courseDAO = new CourseDAOImpl();

    @Override
    public void createStudent(String name, int age) {
        Student student = new Student(name, age);
        studentDAO.save(student);
    }

    @Override
    public void updateStudent(int id, String name, int age) {
        Student student = studentDAO.findById(id);
        if (student != null) {
            student.setName(name);
            student.setAge(age);
            studentDAO.update(student);
        } else {
            System.out.println("Student with ID " + id + " not found.");
        }
    }

    @Override
    public void deleteStudent(int id) {
        Student student = studentDAO.findById(id);
        if (student != null) {
            studentDAO.delete(id);
        } else {
            System.out.println("Student with ID " + id + " not found.");
        }
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
        Course course = courseDAO.findById(courseId);
        if (student != null && course != null) {
            student.addCourse(course);
            studentDAO.update(student);
        } else {
            System.out.println("Student or Course not found.");
        }
    }

    @Override
    public void removeStudentFromCourse(int studentId, int courseId) {
        Student student = studentDAO.findById(studentId);
        Course course = courseDAO.findById(courseId);
        if (student != null && course != null) {
            student.removeCourse(course);
            studentDAO.update(student);
        } else {
            System.out.println("Student or Course not found.");
        }
    }

    @Override
    public Set<Course> getCoursesOfStudent(int studentId) {
        Student student = studentDAO.findById(studentId);
        if (student != null) {
            return student.getCourses();
        }
        return Collections.emptySet();
    }

    @Override
    public List<Student> getStudentsOlderThan(int age) {
        return studentDAO.findOlderThan(age);
    }

    @Override
    public List<Student> getStudentsByName(String name) {
        return studentDAO.findByName(name);
    }

    @Override
    public List<Object[]> getAllStudentsWithCourses() {
        return studentDAO.findAllWithCourses();
    }

    @Override
    public List<Student> getStudentsEnrolledInCourse(int courseId) {
        return studentDAO.findEnrolledInCourse(courseId);
    }
}
