package com.example.service;

import com.example.entity.Course;
import java.util.List;

public interface CourseService {
    void createCourse(String title, int credit);
    void updateCourse(int id, String title, int credit);
    void deleteCourse(int id);
    Course getCourseById(int id);
    List<Course> getAllCourses();
    List<com.example.entity.Student> getStudentsOfCourse(int courseId);

    // Advanced queries
    List<Course> findCoursesWithCreditGreaterThan(int value);
    List<Object[]> countStudentsOfCourse();
}
