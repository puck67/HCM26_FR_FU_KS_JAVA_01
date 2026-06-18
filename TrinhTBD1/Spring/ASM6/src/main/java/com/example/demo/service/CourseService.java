package com.example.demo.service;

import com.example.demo.model.Course;
import com.example.demo.model.Category;
import com.example.demo.service.base.GenericService;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseService extends GenericService<Course, Long> {

    Course saveCourse(Course course);

    Course getCourseById(Long id);

    void deleteCourse(Long id);

    Page<Course> getPublishedCourses(Pageable pageable);

    Page<Course> getCoursesByCategory(String category, Pageable pageable);

    Page<Course> getAllCourses(Pageable pageable);

    Course updateCourseStatus(Long id, Integer status);

    List<Category> getAllCategories();

    void recalculateCategoryFrequencies();

    long countCoursesByStatus(Integer status);
}
