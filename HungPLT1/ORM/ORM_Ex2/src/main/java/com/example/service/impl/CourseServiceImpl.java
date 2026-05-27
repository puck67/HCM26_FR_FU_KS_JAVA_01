package com.example.service.impl;

import com.example.dao.CourseDAO;
import com.example.dao.impl.CourseDAOImpl;
import com.example.entity.Course;
import com.example.entity.Student;
import com.example.service.CourseService;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CourseServiceImpl implements CourseService {

    private final CourseDAO courseDAO = new CourseDAOImpl();

    @Override
    public void createCourse(String title, int credit) {
        Course course = new Course(title, credit);
        courseDAO.save(course);
    }

    @Override
    public void updateCourse(int id, String title, int credit) {
        Course course = courseDAO.findById(id);
        if (course != null) {
            course.setTitle(title);
            course.setCredit(credit);
            courseDAO.update(course);
        } else {
            System.out.println("Course with ID " + id + " not found.");
        }
    }

    @Override
    public void deleteCourse(int id) {
        Course course = courseDAO.findById(id);
        if (course != null) {
            courseDAO.delete(id);
        } else {
            System.out.println("Course with ID " + id + " not found.");
        }
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
        return courseDAO.findCoursesWithCreditGreaterThan(credit);
    }

    @Override
    public Map<String, Long> getStudentCountPerCourse() {
        return courseDAO.getStudentCountPerCourse();
    }
}
