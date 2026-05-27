package com.example.service;

import com.example.entity.Course;
import com.example.entity.Student;

import java.util.List;
import java.util.Map;

/**
 * CourseService provides business operations around Course management and enrollment.
 */
public interface CourseService {
    void createCourse(String title, int credit);
    boolean updateCourse(int id, String title, int credit);
    boolean deleteCourse(int id);
    Course getCourse(int id);
    List<Course> getAllCourses();
    List<Course> getCoursesWithCreditGreaterThan(int credit);
    Map<String, Long> getStudentCountPerCourse();

    // Enrollment operations
    void enrollStudent(int studentId, int courseId);
    void removeStudentFromCourse(int studentId, int courseId);
    List<Course> getCoursesOfStudent(int studentId);
    List<Student> getStudentsOfCourse(int courseId);
}
