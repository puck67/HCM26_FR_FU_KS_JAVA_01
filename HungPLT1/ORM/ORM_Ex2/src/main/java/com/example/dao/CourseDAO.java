package com.example.dao;

import com.example.entity.Course;
import java.util.List;
import java.util.Map;

public interface CourseDAO {
    void save(Course course);
    void update(Course course);
    void delete(int courseId);
    Course findById(int id);
    List<Course> findAll();
    
    // Advanced Queries
    List<Course> findCoursesWithCreditGreaterThan(int credit); // Criteria API Query
    Map<String, Long> getStudentCountPerCourse(); // Aggregation Query: returns mapping of courseTitle to student count
}
