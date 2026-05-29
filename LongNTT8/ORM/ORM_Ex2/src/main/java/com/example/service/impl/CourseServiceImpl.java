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
    public void createCourse(String title, int credit) {
        Course course = Course.builder().title(title).credit(credit).build();
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
            System.out.println("Course not found.");
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
        Course course = courseDAO.findById(courseId);
        if (course != null) {
            // Because fetching might be lazy, ensure session is open, or it might throw LazyInitializationException if not configured
            // assuming it's eagerly fetched or we handle it appropriately. 
            // In a better approach, we'd fetch join it in DAO. Let's see what happens.
            return course.getStudents();
        }
        return null;
    }
}
