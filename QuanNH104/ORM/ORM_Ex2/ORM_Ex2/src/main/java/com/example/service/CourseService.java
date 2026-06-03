package com.example.service;

import com.example.entity.Course;
import com.example.entity.Student;
import java.util.List;

public interface CourseService {
    void saveCourse(Course course);
    void updateCourse(Course course);
    void deleteCourse(int id);
    Course getCourse(int id);
    List<Course> getAllCourses();
    List<Student> getStudentsOfCourse(int courseId);
    
    // Advanced queries
    List<Course> findCoursesWithCreditGreaterThan(int credit);
    List<Object[]> getStudentCountPerCourse();
}
