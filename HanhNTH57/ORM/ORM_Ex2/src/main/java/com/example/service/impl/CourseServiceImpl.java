package com.example.service.impl;

import com.example.dao.CourseDAO;
import com.example.dao.impl.CourseDAOImpl;
import com.example.entity.Course;
import com.example.entity.Student;
import com.example.service.CourseService;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CourseServiceImpl implements CourseService {

    private final CourseDAO courseDAO = new CourseDAOImpl();

    @Override
    public void createCourse(String title, int credit) {
        Course course = new Course(title, credit);
        courseDAO.save(course);
        System.out.println("Course created successfully.");
    }

    @Override
    public void updateCourse(int id, String title, int credit) {
        Course course = courseDAO.findById(id);
        if (course != null) {
            course.setTitle(title);
            course.setCredit(credit);
            courseDAO.update(course);
            System.out.println("Course updated successfully.");
        } else {
            System.err.println("Course not found.");
        }
    }

    @Override
    public void deleteCourse(int id) {
        courseDAO.delete(id);
        System.out.println("Course deleted successfully.");
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
        return (course != null) ? course.getStudents() : null;
    }

    @Override
    public List<Course> getCoursesWithCreditGreaterThan(int credit) {
        return courseDAO.findByCreditGreaterThan(credit);
    }

    @Override
    public void displayStudentCountPerCourse() {
        Map<String, Long> counts = courseDAO.countStudentsInEachCourse();
        StringBuilder sb = new StringBuilder();
        sb.append("--- Student Enrollment Count ---\n");
        
        // Using Stream (implicit in Map forEach) 
        counts.forEach((title, count) -> {
            sb.append("Course: ").append(title)
              .append(" | Students enrolled: ").append(count)
              .append("\n");
        });
        
        System.out.print(sb.toString());
    }
}
