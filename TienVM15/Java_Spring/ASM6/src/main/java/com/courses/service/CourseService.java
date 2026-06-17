package com.courses.service;

import com.courses.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CourseService {
    Page<Course> getPublishedCourses(Pageable pageable);
    Page<Course> getPublishedCoursesByCategory(String category, Pageable pageable);
    List<Course> getAllCourses();
    Course getCourseById(Long id);
    Course saveCourse(Course course);
    void deleteCourse(Long id);
    void updateCategoryFrequencies();
    long countAll();
    long countPublished();
}
