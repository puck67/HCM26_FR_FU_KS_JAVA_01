package com.example.service.impl;

import com.example.dao.CourseDAO;
import com.example.dao.impl.CourseDAOImpl;
import com.example.entity.Course;
import com.example.entity.Student;
import com.example.service.CourseService;
import com.example.util.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class CourseServiceImpl implements CourseService {
    private final CourseDAO courseDAO = new CourseDAOImpl();

    @Override
    public void createCourse(String title, int credit) {
        courseDAO.save(new Course(title, credit));
    }

    @Override
    public void updateCourse(int id, String title, int credit) {
        Course course = courseDAO.findById(id);
        if (course != null) {
            course.setTitle(title);
            course.setCredit(credit);
            courseDAO.update(course);
        } else {
            System.out.println("❌ Không tìm thấy môn học với ID: " + id);
        }
    }

    @Override
    public void deleteCourse(int id) {
        if (courseDAO.findById(id) != null) {
            courseDAO.delete(id);
        } else {
            System.out.println("❌ Không tìm thấy môn học với ID: " + id);
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
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Course course = session.get(Course.class, courseId);
            if (course != null) {
                Hibernate.initialize(course.getStudents());
                return course.getStudents();
            }
            return Collections.emptySet();
        }
    }

    @Override public List<Course> findCoursesWithCreditGreaterThan(int creditValue) { return courseDAO.findCoursesWithCreditGreaterThan(creditValue); }
    @Override public List<Object[]> countStudentsPerCourse() { return courseDAO.countStudentsPerCourse(); }
}
