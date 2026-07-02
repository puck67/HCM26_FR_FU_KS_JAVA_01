package com.fsoft.training.courseshare.service;

import com.fsoft.training.courseshare.entity.Course;
import com.fsoft.training.courseshare.repository.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class CourseService {

    private static final Logger log = LoggerFactory.getLogger(CourseService.class);

    public static final int STATUS_DRAFT = 1;
    public static final int STATUS_PUBLISHED = 2;
    public static final int STATUS_ARCHIVED = 3;

    private final CourseRepository courseRepository;
    private final CategoryService categoryService;

    public CourseService(CourseRepository courseRepository, CategoryService categoryService) {
        this.courseRepository = courseRepository;
        this.categoryService = categoryService;
    }

    public Page<Course> fetchPublishedCourses(Pageable pageable) {
        log.debug("Fetching published courses: pageable={}", pageable);
        return courseRepository.findByStatusOrderByCreatedAtDesc(STATUS_PUBLISHED, pageable);
    }

    public Page<Course> fetchPublishedCoursesByCategory(String category, Pageable pageable) {
        log.debug("Fetching published courses by category '{}': pageable={}", category, pageable);
        return courseRepository.findByStatusAndCategoryContainingIgnoreCaseOrderByCreatedAtDesc(STATUS_PUBLISHED, category, pageable);
    }

    public Page<Course> fetchAllCourses(Pageable pageable) {
        log.debug("Fetching all courses: pageable={}", pageable);
        return courseRepository.findAll(pageable);
    }

    public List<Course> fetchAllCourses() {
        log.debug("Fetching all courses list");
        return courseRepository.findAll();
    }

    public Course findCourseById(Long id) {
        log.debug("Finding course by id: {}", id);
        return courseRepository.findById(id).orElse(null);
    }

    @Transactional
    public void saveCourseEntity(Course course) {
        log.info("Saving course: {}", course.getTitle());
        Course existing = null;
        if (course.getId() != null) {
            existing = courseRepository.findById(course.getId()).orElse(null);
        }

        // Process categories
        if (existing != null && existing.getCategory() != null) {
            Arrays.stream(existing.getCategory().split(","))
                    .map(String::trim)
                    .filter(cat -> !cat.isEmpty())
                    .forEach(categoryService::removeCategoryFrequency);
        }

        if (course.getCategory() != null && !course.getCategory().trim().isEmpty()) {
            Arrays.stream(course.getCategory().split(","))
                    .map(String::trim)
                    .filter(cat -> !cat.isEmpty())
                    .forEach(categoryService::addCategoryFrequency);
        }

        courseRepository.save(course);
        log.info("Course saved successfully: {}", course.getId());
    }

    @Transactional
    public void deleteCourseById(Long id) {
        log.info("Deleting course by id: {}", id);
        Course course = courseRepository.findById(id).orElse(null);
        if (course != null) {
            if (course.getCategory() != null && !course.getCategory().trim().isEmpty()) {
                Arrays.stream(course.getCategory().split(","))
                        .map(String::trim)
                        .filter(cat -> !cat.isEmpty())
                        .forEach(categoryService::removeCategoryFrequency);
            }
            courseRepository.deleteById(id);
            log.info("Course deleted successfully: {}", id);
        } else {
            log.warn("Course with id {} not found for deletion", id);
        }
    }

    public long countTotalCourses() {
        return courseRepository.count();
    }

    public long countPublishedCourses() {
        return courseRepository.countByStatus(STATUS_PUBLISHED);
    }
}
