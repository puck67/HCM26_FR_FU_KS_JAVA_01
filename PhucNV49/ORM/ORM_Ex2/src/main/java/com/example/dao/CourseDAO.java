package com.example.dao;

import com.example.entity.Course;
import java.util.List;

public interface CourseDAO {
    void save(Course course);
    void update(Course course);
    void delete(int courseId);
    Course findById(int id);
    List<Course> findAll();
    
    // Task 6: Advanced queries
    List<Course> findByCreditGreaterThanCriteria(int credit); // Criteria API Query
    List<Object[]> getStudentCountPerCourse(); // Aggregation Query
}
