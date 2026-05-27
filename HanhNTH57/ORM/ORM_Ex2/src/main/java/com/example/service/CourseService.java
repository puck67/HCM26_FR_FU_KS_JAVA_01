package com.example.service;

import com.example.entity.Course;
import com.example.entity.Student;
import java.util.List;
import java.util.Set;

public interface CourseService {
    void createCourse(String title, int credit);
    void updateCourse(int id, String title, int credit);
    void deleteCourse(int id);
    Course getCourseById(int id);
    List<Course> getAllCourses();
    
    Set<Student> getStudentsOfCourse(int courseId);
    
    // Advanced Queries
    List<Course> getCoursesWithCreditGreaterThan(int credit);
    void displayStudentCountPerCourse();
}
