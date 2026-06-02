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
    public Student createStudent(String name, int age) {
        com.example.util.Validator.validateStudentName(name);
        com.example.util.Validator.validateStudentAge(age);
        Student student = new Student(name, age);
        studentDAO.save(student);
        return student;
    }

    @Override
    public Student updateStudent(int id, String name, int age) {
        com.example.util.Validator.validateStudentName(name);
        com.example.util.Validator.validateStudentAge(age);
        Student student = studentDAO.findById(id);
        if (student != null) {
            student.setName(name);
            student.setAge(age);
            studentDAO.update(student);
        }
        return student;
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
        Student student = studentDAO.findById(studentId);
        Course course = courseDAO.findById(courseId);
        if (student != null && course != null) {
            student.addCourse(course);
            studentDAO.update(student);
        }
    }

    @Override
    public void removeStudentFromCourse(int studentId, int courseId) {
        Student student = studentDAO.findById(studentId);
        Course course = courseDAO.findById(courseId);
        if (student != null && course != null) {
            student.removeCourse(course);
            studentDAO.update(student);
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
    public List<Object[]> getStudentsWithCourseTitles() {
        return studentDAO.findStudentsWithCourseTitles();
    }

    @Override
    public List<Student> getStudentsEnrolledInCourse(int courseId) {
        return studentDAO.findStudentsByCourse(courseId);
    }
}
