package com.example.asm6.service.impl;

import com.example.asm6.model.Course;
import com.example.asm6.repository.CourseRepository;
import com.example.asm6.service.CategoryService;
import com.example.asm6.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CategoryService categoryService;

    @Override
    public Page<Course> getPublishedCourses(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return courseRepository.findByStatus(2, pageable);
    }

    @Override
    public Page<Course> getPublishedCoursesByCategory(String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return courseRepository.findByStatusAndCategoryLike(2, category, pageable);
    }

    @Override
    public Page<Course> getAllCourses(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return courseRepository.findAll(pageable);
    }

    @Override
    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + id));
    }

    @Override
    @Transactional
    public Course createCourse(Course course) {
        course.setCreatedAt(LocalDateTime.now());
        if (course.getStatus() == null) {
            course.setStatus(1); // Mặc định là DRAFT
        }
        Course savedCourse = courseRepository.save(course);
        categoryService.recalculateCategoryFrequencies();
        return savedCourse;
    }

    @Override
    @Transactional
    public Course updateCourse(Long id, Course courseDetails) {
        Course course = getCourseById(id);
        course.setTitle(courseDetails.getTitle());
        course.setDescription(courseDetails.getDescription());
        course.setContent(courseDetails.getContent());
        course.setStatus(courseDetails.getStatus());
        course.setCategory(courseDetails.getCategory());
        
        Course updatedCourse = courseRepository.save(course);
        categoryService.recalculateCategoryFrequencies();
        return updatedCourse;
    }

    @Override
    @Transactional
    public void deleteCourse(Long id) {
        Course course = getCourseById(id);
        courseRepository.delete(course);
        categoryService.recalculateCategoryFrequencies();
    }

    @Override
    @Transactional
    public Course changeCourseStatus(Long id, Integer status) {
        Course course = getCourseById(id);
        course.setStatus(status);
        Course updatedCourse = courseRepository.save(course);
        categoryService.recalculateCategoryFrequencies();
        return updatedCourse;
    }
}
