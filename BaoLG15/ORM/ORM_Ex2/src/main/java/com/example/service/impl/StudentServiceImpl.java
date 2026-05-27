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

    private final StudentDAO studentDAO;
    private final CourseDAO courseDAO;

    public StudentServiceImpl() {
        this.studentDAO = new StudentDAOImpl();
        this.courseDAO = new CourseDAOImpl();
    }

    public StudentServiceImpl(StudentDAO studentDAO, CourseDAO courseDAO) {
        this.studentDAO = studentDAO;
        this.courseDAO = courseDAO;
    }

    @Override
    public Student createStudent(String name, int age) {
        Student student = new Student(name, age);
        studentDAO.save(student);
        return student;
    }

    @Override
    public void updateStudent(int id, String name, int age) {
        Student student = studentDAO.findById(id);
        if (student != null) {
            student.setName(name);
            student.setAge(age);
            studentDAO.update(student);
        } else {
            throw new IllegalArgumentException("Student not found with ID: " + id);
        }
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
        Student student = studentDAO.findByIdWithCourses(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Student not found with ID: " + studentId);
        }
        Course course = courseDAO.findByIdWithStudents(courseId);
        if (course == null) {
            throw new IllegalArgumentException("Course not found with ID: " + courseId);
        }
        if (student.getCourses().contains(course)) {
            throw new IllegalArgumentException("Student is already enrolled in this course");
        }
        student.addCourse(course);
        studentDAO.update(student);
    }

    @Override
    public void removeStudentFromCourse(int studentId, int courseId) {
        Student student = studentDAO.findByIdWithCourses(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Student not found with ID: " + studentId);
        }
        Course course = courseDAO.findByIdWithStudents(courseId);
        if (course == null) {
            throw new IllegalArgumentException("Course not found with ID: " + courseId);
        }
        if (!student.getCourses().contains(course)) {
            throw new IllegalArgumentException("Student is not enrolled in this course");
        }
        student.removeCourse(course);
        studentDAO.update(student);
    }

    @Override
    public Set<Course> getCoursesOfStudent(int studentId) {
        Student student = studentDAO.findByIdWithCourses(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Student not found with ID: " + studentId);
        }
        return student.getCourses();
    }

    @Override
    public List<Student> findOlderThan(int age) {
        return studentDAO.findOlderThan(age);
    }

    @Override
    public List<Object[]> findStudentsWithCourseTitles() {
        return studentDAO.findStudentsWithCourseTitles();
    }

    @Override
    public List<Student> findByName(String name) {
        return studentDAO.findByName(name);
    }

    @Override
    public List<Student> findStudentsByCourseId(int courseId) {
        return studentDAO.findStudentsByCourseId(courseId);
    }
}
