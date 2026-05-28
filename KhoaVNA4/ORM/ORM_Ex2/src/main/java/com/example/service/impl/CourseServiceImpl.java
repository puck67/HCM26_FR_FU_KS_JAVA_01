package com.example.service.impl;

import com.example.dao.CourseDAO;
import com.example.dao.StudentDAO;
import com.example.dao.impl.CourseDAOImpl;
import com.example.dao.impl.StudentDAOImpl;
import com.example.entity.Course;
import com.example.entity.Student;
import com.example.service.CourseService;
import java.util.List;
import java.util.Set;
import java.util.Collections;

public class CourseServiceImpl extends GenericServiceImpl<Course, Integer, CourseDAO> implements CourseService {
    private final StudentDAO studentDAO;

    public CourseServiceImpl() {
        super(new CourseDAOImpl());
        this.studentDAO = new StudentDAOImpl();
    }

    public CourseServiceImpl(CourseDAO courseDAO, StudentDAO studentDAO) {
        super(courseDAO);
        this.studentDAO = studentDAO;
    }

    @Override
    public void createCourse(String title, int credit) {
        Course course = new Course(title, credit);
        dao.save(course);
    }

    @Override
    public void updateCourse(int id, String title, int credit) {
        Course course = dao.findById(id);
        if (course != null) {
            course.setTitle(title);
            course.setCredit(credit);
            dao.update(course);
        }
    }

    @Override
    public void deleteCourse(int id) {
        Course course = dao.findById(id);
        if (course != null) {
            for (Student s : course.getStudents()) {
                s.getCourses().remove(course);
                studentDAO.update(s);
            }
            course.getStudents().clear();
            dao.update(course);
            dao.delete(id);
        }
    }

    @Override
    public Course getCourseById(int id) {
        return dao.findById(id);
    }

    @Override
    public List<Course> getAllCourses() {
        return dao.findAll();
    }

    @Override
    public Set<Student> getStudentsOfCourse(int courseId) {
        Course course = dao.findById(courseId);
        if (course != null) {
            return course.getStudents();
        }
        return Collections.emptySet();
    }

    @Override
    public List<Course> findCoursesWithCreditGreaterThan(int credit) {
        return dao.findCoursesWithCreditGreaterThan(credit);
    }

    @Override
    public List<Object[]> countStudentsEnrolledInEachCourse() {
        return dao.countStudentsEnrolledInEachCourse();
    }
}
