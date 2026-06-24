package com.fsoft.training.courseshare.service;

import com.fsoft.training.courseshare.entity.Course;
import com.fsoft.training.courseshare.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseService {
    public static final int STATUS_DRAFT = 1;
    public static final int STATUS_PUBLISHED = 2;
    public static final int STATUS_ARCHIVED = 3;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CategoryService categoryService;

    public Page<Course> getPublishedCourses(Pageable pageable) {
        return courseRepository.findByStatusOrderByCreatedAtDesc(STATUS_PUBLISHED, pageable);
    }

    public Page<Course> getPublishedCoursesByCategory(String category, Pageable pageable) {
        return courseRepository.findByStatusAndCategoryContainingIgnoreCaseOrderByCreatedAtDesc(STATUS_PUBLISHED, category, pageable);
    }

    public Page<Course> getAllCourses(Pageable pageable) {
        return courseRepository.findAll(pageable);
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElse(null);
    }

    @Transactional
    public void saveCourse(Course course) {
        Course existing = null;
        if (course.getId() != null) {
            existing = courseRepository.findById(course.getId()).orElse(null);
        }

        // Process categories
        if (existing != null && existing.getCategory() != null) {
            String[] oldCats = existing.getCategory().split(",");
            for (String cat : oldCats) {
                categoryService.decrementCategoryFrequency(cat);
            }
        }

        if (course.getCategory() != null && !course.getCategory().trim().isEmpty()) {
            String[] newCats = course.getCategory().split(",");
            for (String cat : newCats) {
                categoryService.incrementCategoryFrequency(cat);
            }
        }

        courseRepository.save(course);
    }

    @Transactional
    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id).orElse(null);
        if (course != null) {
            if (course.getCategory() != null && !course.getCategory().trim().isEmpty()) {
                String[] cats = course.getCategory().split(",");
                for (String cat : cats) {
                    categoryService.decrementCategoryFrequency(cat);
                }
            }
            courseRepository.deleteById(id);
        }
    }

    public long getTotalCourses() {
        return courseRepository.count();
    }

    public long getPublishedCoursesCount() {
        return courseRepository.countByStatus(STATUS_PUBLISHED);
    }
}
