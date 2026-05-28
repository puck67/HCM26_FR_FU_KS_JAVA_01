package com.example.service.impl;

import com.example.dao.CourseDAO;
import com.example.dao.impl.CourseDAOImpl;
import com.example.entity.Course;
import com.example.entity.Student;
import com.example.service.CourseService;

import java.util.List;
import java.util.Set;

public class CourseServiceImpl implements CourseService {

    private final CourseDAO courseDAO;

    public CourseServiceImpl() {
        this.courseDAO = new CourseDAOImpl();
    }

    public CourseServiceImpl(CourseDAO courseDAO) {
        this.courseDAO = courseDAO;
    }

    @Override
    public Course createCourse(String title, int credit) {
        Course course = new Course(title, credit);
        courseDAO.save(course);
        return course;
    }

    @Override
    public void updateCourse(int id, String title, int credit) {
        Course course = courseDAO.findById(id);
        if (course != null) {
            course.setTitle(title);
            course.setCredit(credit);
            courseDAO.update(course);
        } else {
            throw new IllegalArgumentException("Course not found with ID: " + id);
        }
    }

    @Override
    public void deleteCourse(int id) {
        courseDAO.delete(id);
    }

    @Override
    public Course getCourseById(int id) {
        return courseDAO.findById(id);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseDAO.findAll();
    }

    @Override
    public Set<Student> getStudentsOfCourse(int courseId) {
        Course course = courseDAO.findByIdWithStudents(courseId);
        if (course == null) {
            throw new IllegalArgumentException("Course not found with ID: " + courseId);
        }
        return course.getStudents();
    }

    @Override
    public List<Course> findCoursesWithCreditGreaterThan(int credit) {
        return courseDAO.findCoursesWithCreditGreaterThan(credit);
    }

    @Override
    public List<Object[]> countStudentsPerCourse() {
        return courseDAO.countStudentsPerCourse();
    }
}
