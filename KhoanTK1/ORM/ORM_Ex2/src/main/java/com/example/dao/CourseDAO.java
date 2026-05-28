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
    List<Course> findCoursesWithCreditGreaterThan(int credit);
    List<Object[]> countStudentsInEachCourse();
}
