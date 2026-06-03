package com.example.service.impl;

import com.example.dao.CourseDAO;
import com.example.dao.impl.CourseDAOImpl;
import com.example.entity.Course;
import com.example.entity.Student;
import com.example.service.CourseService;
import com.example.validation.Validator;
import java.util.ArrayList;
import java.util.List;

public class CourseServiceImpl implements CourseService {

    private final CourseDAO courseDAO = new CourseDAOImpl();

    @Override
    public void saveCourse(Course course) {
        Validator.validateCourse(course.getTitle(), course.getCredit());
        courseDAO.save(course);
    }

    @Override
    public void updateCourse(Course course) {
        Course existing = courseDAO.findById(course.getId());
        if (existing == null) {
            throw new IllegalArgumentException("Course not found with ID " + course.getId());
        }
        Validator.validateCourse(course.getTitle(), course.getCredit());
        courseDAO.update(course);
    }

    @Override
    public void deleteCourse(int id) {
        Course existing = courseDAO.findById(id);
        if (existing == null) {
            throw new IllegalArgumentException("Course not found with ID " + id);
        }
        courseDAO.delete(id);
    }

    @Override
    public Course getCourse(int id) {
        Course course = courseDAO.findById(id);
        if (course == null) {
            throw new IllegalArgumentException("Course not found with ID " + id);
        }
        return course;
    }

    @Override
    public List<Course> getAllCourses() {
        return courseDAO.findAll();
    }

    @Override
    public List<Student> getStudentsOfCourse(int courseId) {
        Course course = courseDAO.findById(courseId);
        if (course == null) {
            throw new IllegalArgumentException("Course with ID " + courseId + " does not exist!");
        }
        return new ArrayList<>(course.getStudents());
    }

    @Override
    public List<Course> findCoursesWithCreditGreaterThan(int credit) {
        return courseDAO.findCoursesByCreditGreaterThan(credit);
    }

    @Override
    public List<Object[]> getStudentCountPerCourse() {
        return courseDAO.countStudentsPerCourse();
    }
}
