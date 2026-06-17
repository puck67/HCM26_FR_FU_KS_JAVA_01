package com.courses.service.impl;

import com.courses.entity.Category;
import com.courses.entity.Course;
import com.courses.repository.CategoryRepository;
import com.courses.repository.CourseRepository;
import com.courses.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Page<Course> getPublishedCourses(Pageable pageable) {
        return courseRepository.findByStatusOrderByIdDesc(2, pageable);
    }

    @Override
    public Page<Course> getPublishedCoursesByCategory(String category, Pageable pageable) {
        return courseRepository.findByStatusAndCategoryLike(2, category, pageable);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAllByOrderByIdDesc();
    }

    @Override
    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
    }

    @Override
    public Course saveCourse(Course course) {
        Course savedCourse = courseRepository.save(course);
        updateCategoryFrequencies();
        return savedCourse;
    }

    @Override
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
        updateCategoryFrequencies();
    }

    @Override
    public void updateCategoryFrequencies() {
        // Clear all categories first
        categoryRepository.deleteAll();

        // Retrieve all published courses
        List<Course> courses = courseRepository.findAll();
        Map<String, Integer> freqMap = new HashMap<>();

        for (Course course : courses) {
            // Only count published course categories
            if (course.getStatus() == 2 && course.getCategory() != null) {
                String[] tags = course.getCategory().split(",");
                for (String tag : tags) {
                    String cleanTag = tag.trim();
                    if (!cleanTag.isEmpty()) {
                        freqMap.put(cleanTag, freqMap.getOrDefault(cleanTag, 0) + 1);
                    }
                }
            }
        }

        // Save new frequency map
        for (Map.Entry<String, Integer> entry : freqMap.entrySet()) {
            categoryRepository.save(Category.builder()
                    .name(entry.getKey())
                    .frequency(entry.getValue())
                    .build());
        }
    }

    @Override
    public long countAll() {
        return courseRepository.count();
    }

    @Override
    public long countPublished() {
        return courseRepository.findAll().stream().filter(c -> c.getStatus() == 2).count();
    }
}
