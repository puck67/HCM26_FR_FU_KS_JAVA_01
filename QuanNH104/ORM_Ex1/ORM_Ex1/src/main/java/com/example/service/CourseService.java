package com.example.service;

import com.example.dao.CourseDAO;
import com.example.model.Course;
import com.example.model.Student;
import com.example.validation.Validator;

import java.util.List;

public class CourseService {
    private final CourseDAO courseDAO = new CourseDAO();

    public void saveCourse(Course course) {
        Validator.validateCourse(course.getTitle(), course.getCredit());
        courseDAO.saveCourse(course);
    }

    public void updateCourse(Course course) {
        Course existing = courseDAO.getCourse(course.getId());
        if (existing == null) {
            throw new IllegalArgumentException("Course not found with ID " + course.getId());
        }
        Validator.validateCourse(course.getTitle(), course.getCredit());
        courseDAO.updateCourse(course);
    }

    public void deleteCourse(int id) {
        Course existing = courseDAO.getCourse(id);
        if (existing == null) {
            throw new IllegalArgumentException("Course not found with ID " + id);
        }
        courseDAO.deleteCourse(id);
    }

    public Course getCourse(int id) {
        Course course = courseDAO.getCourse(id);
        if (course == null) {
            throw new IllegalArgumentException("Course not found with ID " + id);
        }
        return course;
    }

    public List<Course> getAllCourses() {
        return courseDAO.getAllCourses();
    }

    public List<Student> getStudentsOfCourse(int courseId) {
        return courseDAO.getStudentsOfCourse(courseId);
    }
}
