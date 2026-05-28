package com.example.dao;

import com.example.entity.Course;
import java.util.List;

public interface CourseDAO {
    void save(Course course);
    void update(Course course);
    void delete(int courseId);
    Course findById(int id);
    List<Course> findAll();

    // Advanced Queries
    List<Course> findWithCreditGreaterThan(int credit);
    List<Object[]> getEnrollmentCounts();
}
