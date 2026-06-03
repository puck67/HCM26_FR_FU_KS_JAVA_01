package com.example.service.impl;

import com.example.dao.CourseDAO;
import com.example.dao.StudentDAO;
import com.example.dao.impl.CourseDAOImpl;
import com.example.dao.impl.StudentDAOImpl;
import com.example.entity.Course;
import com.example.entity.Student;
import com.example.service.StudentService;

import java.util.ArrayList;
import java.util.List;

public class StudentServiceImpl implements StudentService {

    private final StudentDAO studentDAO = new StudentDAOImpl();
    private final CourseDAO courseDAO = new CourseDAOImpl();

    @Override
    public Student createStudent(String name, int age) {
        Student student = new Student(name, age);
        studentDAO.save(student);
        return student;
    }

    @Override
    public Student updateStudent(int id, String name, int age) {
        Student student = studentDAO.findById(id);
        if (student == null) {
            throw new RuntimeException("Student not found with id: " + id);
        }
        student.setName(name);
        student.setAge(age);
        studentDAO.update(student);
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
        if (student == null) {
            throw new RuntimeException("Student not found with id: " + studentId);
        }
        if (course == null) {
            throw new RuntimeException("Course not found with id: " + courseId);
        }
        student.getCourses().add(course);
        studentDAO.update(student);
    }

    @Override
    public void removeStudentFromCourse(int studentId, int courseId) {
        Student student = studentDAO.findById(studentId);
        Course course = courseDAO.findById(courseId);
        if (student == null) {
            throw new RuntimeException("Student not found with id: " + studentId);
        }
        if (course == null) {
            throw new RuntimeException("Course not found with id: " + courseId);
        }
        student.getCourses().remove(course);
        studentDAO.update(student);
    }

    @Override
    public List<Course> getCoursesOfStudent(int studentId) {
        Student student = studentDAO.findById(studentId);
        if (student == null) {
            throw new RuntimeException("Student not found with id: " + studentId);
        }
        return new ArrayList<>(student.getCourses());
    }

    @Override
    public List<Student> findStudentsOlderThan(int age) {
        return studentDAO.findByAge(age);
    }

    @Override
    public List<Student> findStudentsByName(String name) {
        return studentDAO.findByName(name);
    }

    @Override
    public List<Object[]> getStudentsWithCourses() {
        return studentDAO.findStudentsWithCourses();
    }

    @Override
    public List<Student> findStudentsByCourseId(int courseId) {
        return studentDAO.findStudentsByCourseId(courseId);
    }

    @Override
    public List<Object[]> countStudentsPerCourse() {
        return studentDAO.countStudentsPerCourse();
    }
}
