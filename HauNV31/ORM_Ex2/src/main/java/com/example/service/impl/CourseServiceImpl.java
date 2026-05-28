package com.example.service.impl;

import com.example.dao.CourseDAO;
import com.example.dao.impl.CourseDAOImpl;
import com.example.entity.Course;
import com.example.entity.Student;
import com.example.service.CourseService;

import java.util.ArrayList;
import java.util.List;

public class CourseServiceImpl implements CourseService {

    private final CourseDAO courseDAO = new CourseDAOImpl();

    @Override
    public Course createCourse(String title, int credit) {
        Course course = new Course(title, credit);
        courseDAO.save(course);
        return course;
    }

    @Override
    public Course updateCourse(int id, String title, int credit) {
        Course course = courseDAO.findById(id);
        if (course == null) {
            throw new RuntimeException("Course not found with id: " + id);
        }
        course.setTitle(title);
        course.setCredit(credit);
        courseDAO.update(course);
        return course;
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
    public List<Student> getStudentsOfCourse(int courseId) {
        Course course = courseDAO.findById(courseId);
        if (course == null) {
            throw new RuntimeException("Course not found with id: " + courseId);
        }
        return new ArrayList<>(course.getStudents());
    }

    @Override
    public List<Course> findCoursesWithCreditGreaterThan(int credit) {
        return courseDAO.findByCreditGreaterThan(credit);
    }
}
