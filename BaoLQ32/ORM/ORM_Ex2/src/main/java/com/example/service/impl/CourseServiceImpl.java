package com.example.service.impl;

import com.example.dao.CourseDAO;
import com.example.dao.StudentDAO;
import com.example.dao.impl.CourseDAOImpl;
import com.example.dao.impl.StudentDAOImpl;
import com.example.entity.Course;
import com.example.entity.Student;
import com.example.service.CourseService;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class CourseServiceImpl implements CourseService {

    private final CourseDAO courseDAO = new CourseDAOImpl();
    private final StudentDAO studentDAO = new StudentDAOImpl();

    @Override
    public Course createCourse(String title, int credit) {
        com.example.util.Validator.validateCourseTitle(title);
        com.example.util.Validator.validateCourseCredit(credit);
        Course course = new Course(title, credit);
        courseDAO.save(course);
        return course;
    }

    @Override
    public Course updateCourse(int id, String title, int credit) {
        com.example.util.Validator.validateCourseTitle(title);
        com.example.util.Validator.validateCourseCredit(credit);
        Course course = courseDAO.findById(id);
        if (course != null) {
            course.setTitle(title);
            course.setCredit(credit);
            courseDAO.update(course);
        }
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
    public Set<Student> getStudentsOfCourse(int courseId) {
        Course course = courseDAO.findById(courseId);
        if (course != null) {
            return course.getStudents();
        }
        return Collections.emptySet();
    }

    @Override
    public List<Course> getCoursesWithCreditGreaterThan(int credit) {
        return courseDAO.findWithCreditGreaterThan(credit);
    }

    @Override
    public List<Object[]> getStudentsCountPerCourse() {
        return courseDAO.countStudentsPerCourse();
    }
}
