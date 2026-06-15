package com.fresheracademy.lms.service;

import com.fresheracademy.lms.entity.Course;
import com.fresheracademy.lms.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private com.fresheracademy.lms.repository.CategoryRepository categoryRepository;

    public Page<Course> getPublishedCourses(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return courseRepository.findByStatusOrderByCreatedAtDesc(2, pageable);
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElse(null);
    }

    public List<Course> getCoursesByCategory(String category) {
        return courseRepository.findByStatusAndCategoryContainingOrderByCreatedAtDesc(2, category);
    }

    public Page<Course> getCoursesByCategory(String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return courseRepository.findByStatusAndCategoryContainingOrderByCreatedAtDesc(2, category, pageable);
    }

    public Course saveCourse(Course course) {
        Course saved = courseRepository.save(course);
        updateCategoryFrequencies();
        return saved;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
    
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
        updateCategoryFrequencies();
    }

    @Transactional
    public void updateCategoryFrequencies() {
        categoryRepository.deleteAll();
        // Fetch all published courses
        List<Course> publishedCourses = courseRepository.findByStatusOrderByCreatedAtDesc(2, Pageable.unpaged()).getContent();
        java.util.Map<String, Integer> freqMap = new java.util.HashMap<>();
        for (Course c : publishedCourses) {
            if (c.getCategory() != null && !c.getCategory().trim().isEmpty()) {
                String[] cats = c.getCategory().split(",");
                for (String cat : cats) {
                    String clean = cat.trim();
                    if (!clean.isEmpty()) {
                        freqMap.put(clean, freqMap.getOrDefault(clean, 0) + 1);
                    }
                }
            }
        }
        for (java.util.Map.Entry<String, Integer> entry : freqMap.entrySet()) {
            com.fresheracademy.lms.entity.Category category = new com.fresheracademy.lms.entity.Category();
            category.setName(entry.getKey());
            category.setFrequency(entry.getValue());
            categoryRepository.save(category);
        }
    }
}
