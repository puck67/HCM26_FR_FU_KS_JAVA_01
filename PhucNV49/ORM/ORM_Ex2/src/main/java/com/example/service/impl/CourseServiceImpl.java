package com.example.service.impl;

import com.example.dao.CourseDAO;
import com.example.dao.impl.CourseDAOImpl;
import com.example.entity.Course;
import com.example.entity.Student;
import com.example.service.CourseService;

import java.util.List;
import java.util.Set;

public class CourseServiceImpl implements CourseService {

    private final CourseDAO courseDAO = new CourseDAOImpl();

    @Override
    public Course createCourse(String title, int credit) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Course title cannot be empty.");
        }
        if (credit <= 0) {
            throw new IllegalArgumentException("Course credit must be positive.");
        }
        Course course = new Course(title.trim(), credit);
        courseDAO.save(course);
        return course;
    }

    @Override
    public void updateCourse(int id, String title, int credit) {
        Course course = courseDAO.findById(id);
        if (course == null) {
            throw new IllegalArgumentException("Course with ID " + id + " not found.");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Course title cannot be empty.");
        }
        if (credit <= 0) {
            throw new IllegalArgumentException("Course credit must be positive.");
        }
        course.setTitle(title.trim());
        course.setCredit(credit);
        courseDAO.update(course);
    }

    @Override
    public void deleteCourse(int id) {
        Course course = courseDAO.findById(id);
        if (course == null) {
            throw new IllegalArgumentException("Course with ID " + id + " not found.");
        }
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
        Course course = courseDAO.findById(courseId);
        if (course == null) {
            throw new IllegalArgumentException("Course with ID " + courseId + " not found.");
        }
        return course.getStudents();
    }

    @Override
    public List<Course> findCoursesByCreditGreaterThan(int credit) {
        return courseDAO.findByCreditGreaterThanCriteria(credit);
    }

    @Override
    public List<Object[]> getStudentCountPerCourse() {
        return courseDAO.getStudentCountPerCourse();
    }
}
