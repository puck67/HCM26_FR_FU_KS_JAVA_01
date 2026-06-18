package com.fpt.lms.service;

import com.fpt.lms.entity.*;
import com.fpt.lms.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@Transactional
public class CourseService {

    @Autowired private CourseRepository courseRepository;
    @Autowired private CategoryRepository categoryRepository;

    public Page<Course> getPublishedCourses(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("createdAt").descending());
        return courseRepository.findByStatusOrderByCreatedAtDesc(2, pageable);
    }

    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Course> getCoursesByCategory(String category) {
        return courseRepository.findPublishedByCategory(category);
    }

    public Course save(Course course) {
        Course saved = courseRepository.save(course);
        updateCategories();
        return saved;
    }

    public void delete(Long id) {
        courseRepository.deleteById(id);
        updateCategories();
    }

    public void updateStatus(Long id, Integer status) {
        courseRepository.findById(id).ifPresent(c -> {
            c.setStatus(status);
            courseRepository.save(c);
            updateCategories();
        });
    }

    private void updateCategories() {
        // Rebuild category frequencies from published courses
        List<Course> published = courseRepository.findByStatusOrderByCreatedAtDesc(2, Pageable.unpaged()).getContent();
        Map<String, Integer> freq = new HashMap<>();
        for (Course c : published) {
            if (c.getCategory() != null && !c.getCategory().isBlank()) {
                for (String cat : c.getCategory().split(",")) {
                    String trimmed = cat.trim();
                    if (!trimmed.isEmpty()) {
                        freq.merge(trimmed, 1, Integer::sum);
                    }
                }
            }
        }
        // Update existing
        List<Category> allCats = categoryRepository.findAll();
        Set<String> existingNames = new HashSet<>();
        for (Category cat : allCats) {
            if (freq.containsKey(cat.getName())) {
                cat.setFrequency(freq.get(cat.getName()));
                categoryRepository.save(cat);
                existingNames.add(cat.getName());
            } else {
                categoryRepository.delete(cat);
            }
        }
        // Add new
        for (Map.Entry<String, Integer> e : freq.entrySet()) {
            if (!existingNames.contains(e.getKey())) {
                Category cat = new Category();
                cat.setName(e.getKey());
                cat.setFrequency(e.getValue());
                categoryRepository.save(cat);
            }
        }
    }
}
