package com.example.service.impl;

import com.example.dao.StudentDAO;
import com.example.dao.CourseDAO;
import com.example.dao.impl.StudentDAOImpl;
import com.example.dao.impl.CourseDAOImpl;
import com.example.entity.Course;
import com.example.entity.Student;
import com.example.service.StudentService;
import java.util.List;
import java.util.Set;
import java.util.Collections;

public class StudentServiceImpl extends GenericServiceImpl<Student, Integer, StudentDAO> implements StudentService {
    private final CourseDAO courseDAO;

    public StudentServiceImpl() {
        super(new StudentDAOImpl());
        this.courseDAO = new CourseDAOImpl();
    }

    public StudentServiceImpl(StudentDAO studentDAO, CourseDAO courseDAO) {
        super(studentDAO);
        this.courseDAO = courseDAO;
    }

    @Override
    public void createStudent(String name, int age) {
        Student student = new Student(name, age);
        dao.save(student);
    }

    @Override
    public void updateStudent(int id, String name, int age) {
        Student student = dao.findById(id);
        if (student != null) {
            student.setName(name);
            student.setAge(age);
            dao.update(student);
        }
    }

    @Override
    public void deleteStudent(int id) {
        Student student = dao.findById(id);
        if (student != null) {
            student.getCourses().clear();
            dao.update(student);
            dao.delete(id);
        }
    }

    @Override
    public Student getStudentById(int id) {
        return dao.findById(id);
    }

    @Override
    public List<Student> getAllStudents() {
        return dao.findAll();
    }

    @Override
    public void enrollStudentInCourse(int studentId, int courseId) {
        Student student = dao.findById(studentId);
        Course course = courseDAO.findById(courseId);
        if (student != null && course != null) {
            if (!student.getCourses().contains(course)) {
                student.addCourse(course);
                dao.update(student);
            }
        }
    }

    @Override
    public void removeStudentFromCourse(int studentId, int courseId) {
        Student student = dao.findById(studentId);
        Course course = courseDAO.findById(courseId);
        if (student != null && course != null) {
            if (student.getCourses().contains(course)) {
                student.removeCourse(course);
                dao.update(student);
            }
        }
    }

    @Override
    public Set<Course> getCoursesOfStudent(int studentId) {
        Student student = dao.findById(studentId);
        if (student != null) {
            return student.getCourses();
        }
        return Collections.emptySet();
    }

    @Override
    public List<Student> findStudentsOlderThan(int age) {
        return dao.findStudentsOlderThan(age);
    }

    @Override
    public List<Object[]> findStudentsAndCoursesHqlJoin() {
        return dao.findStudentsAndCoursesHqlJoin();
    }

    @Override
    public List<Student> findByNameNamedQuery(String name) {
        return dao.findByName(name);
    }

    @Override
    public List<Student> findStudentsEnrolledInCourse(int courseId) {
        return dao.findStudentsEnrolledInCourse(courseId);
    }
}
