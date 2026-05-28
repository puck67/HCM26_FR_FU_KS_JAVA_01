package com.example.service.impl;

import com.example.dao.CourseDAO;
import com.example.dao.impl.CourseDAOImpl;
import com.example.entity.Course;
import com.example.entity.Student;
import com.example.service.CourseService;
import com.example.util.HibernateUtil;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class CourseServiceImpl implements CourseService {

    private final CourseDAO courseDAO = new CourseDAOImpl();

    @Override
    public Course createCourse(String title, int credit) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Course title cannot be empty.");
        }
        if (credit <= 0) {
            throw new IllegalArgumentException("Credit must be greater than 0.");
        }
        Course course = new Course(title.trim(), credit);
        courseDAO.save(course);
        return course;
    }

    @Override
    public void updateCourse(int id, String title, int credit) {
        Course existing = courseDAO.findById(id);
        if (existing == null) throw new IllegalArgumentException("Course ID " + id + " not found.");
        if (title == null || title.trim().isEmpty()) throw new IllegalArgumentException("Title cannot be empty.");
        if (credit <= 0) throw new IllegalArgumentException("Credit must be greater than 0.");

        existing.setTitle(title.trim());
        existing.setCredit(credit);
        courseDAO.update(existing);
    }

    @Override
    public void deleteCourse(int id) {
        Course existing = courseDAO.findById(id);
        if (existing == null) throw new IllegalArgumentException("Course ID " + id + " not found.");
        courseDAO.delete(id);
    }

    @Override
    public Course getCourseById(int id) {
        Course course = courseDAO.findById(id);
        if (course == null) throw new IllegalArgumentException("Course ID " + id + " not found.");
        return course;
    }

    @Override
    public List<Course> getAllCourses() {
        return courseDAO.findAll();
    }

    @Override
    public List<Student> getStudentsOfCourse(int courseId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Course course = session.get(Course.class, courseId);
            if (course == null) throw new IllegalArgumentException("Course ID " + courseId + " not found.");
            course.getStudents().size();
            return new ArrayList<>(course.getStudents());
        }
    }

    @Override
    public List<Course> findCoursesWithCreditGreaterThan(int credit) {
        return courseDAO.findCreditGreaterThan(credit);
    }

    @Override
    public List<Object[]> countStudentsPerCourse() {
        return courseDAO.countStudentsPerCourse();
    }
}