package com.example.service.impl;

import com.example.dao.CourseDAO;
import com.example.dao.impl.CourseDAOImpl;
import com.example.entity.Course;
import com.example.entity.Student;
import com.example.service.CourseService;
import com.example.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;
import java.util.Set;

public class CourseServiceImpl implements CourseService {

    private final CourseDAO courseDAO = new CourseDAOImpl();

    @Override
    public Course createCourse(String title, int credit) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Course title cannot be empty");
        }
        if (title.length() < 2 || title.length() > 100) {
            throw new IllegalArgumentException("Course title must be between 2 and 100 characters");
        }
        if (!title.matches("^[\\p{L}0-9\\s\\-\\+\\#\\.]+$")) {
            throw new IllegalArgumentException("Course title contains invalid characters");
        }
        if (credit < 1 || credit > 10) {
            throw new IllegalArgumentException("Course credits must be between 1 and 10");
        }
        Course course = new Course(title, credit);
        courseDAO.save(course);
        return course;
    }

    @Override
    public void updateCourse(int id, String title, int credit) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Course title cannot be empty");
        }
        if (title.length() < 2 || title.length() > 100) {
            throw new IllegalArgumentException("Course title must be between 2 and 100 characters");
        }
        if (!title.matches("^[\\p{L}0-9\\s\\-\\+\\#\\.]+$")) {
            throw new IllegalArgumentException("Course title contains invalid characters");
        }
        if (credit < 1 || credit > 10) {
            throw new IllegalArgumentException("Course credits must be between 1 and 10");
        }
        Course course = courseDAO.findById(id);
        if (course == null) {
            throw new RuntimeException("Course with ID " + id + " does not exist");
        }
        course.setTitle(title);
        course.setCredit(credit);
        courseDAO.update(course);
    }

    @Override
    public void deleteCourse(int id) {
        Course course = courseDAO.findById(id);
        if (course == null) {
            throw new RuntimeException("Course with ID " + id + " does not exist");
        }
        courseDAO.delete(id);
    }

    @Override
    public Course getCourseById(int id) {
        Course course = courseDAO.findById(id);
        if (course == null) {
            throw new RuntimeException("Course with ID " + id + " does not exist");
        }
        return course;
    }

    @Override
    public List<Course> getAllCourses() {
        return courseDAO.findAll();
    }

    @Override
    public Set<Student> getStudentsOfCourse(int courseId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Course course = session.createQuery(
                "select c from Course c left join fetch c.students where c.id = :id", Course.class)
                .setParameter("id", courseId)
                .uniqueResult();
            if (course == null) {
                throw new RuntimeException("Course with ID " + courseId + " does not exist");
            }
            return course.getStudents();
        }
    }

    @Override
    public List<Course> findCoursesWithCreditGreaterThan(int credit) {
        if (credit < 0) {
            throw new IllegalArgumentException("Credit threshold cannot be negative");
        }
        return courseDAO.findWithCreditGreaterThan(credit);
    }

    @Override
    public List<Object[]> getCourseEnrollmentCounts() {
        return courseDAO.getEnrollmentCounts();
    }
}
