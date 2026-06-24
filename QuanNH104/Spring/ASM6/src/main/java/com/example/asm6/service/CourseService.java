package com.example.asm6.service;

import com.example.asm6.model.Course;
import org.springframework.data.domain.Page;

public interface CourseService {
    Page<Course> getPublishedCourses(int page, int size);
    Page<Course> getPublishedCoursesByCategory(String category, int page, int size);
    Page<Course> getAllCourses(int page, int size);
    Course getCourseById(Long id);
    Course createCourse(Course course);
    Course updateCourse(Long id, Course courseDetails);
    void deleteCourse(Long id);
    Course changeCourseStatus(Long id, Integer status);
}
