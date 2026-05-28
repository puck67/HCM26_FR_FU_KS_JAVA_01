package com.example.service;

import com.example.entity.Course;
import com.example.entity.Student;
import java.util.List;

public interface CourseService {
    Course createCourse(String title, int credit);
    void updateCourse(int id, String title, int credit);
    void deleteCourse(int id);
    Course getCourseById(int id);
    List<Course> getAllCourses();
    List<Student> getStudentsOfCourse(int courseId);

    List<Course> findCoursesWithCreditGreaterThan(int credit);
    List<Object[]> countStudentsPerCourse();
}