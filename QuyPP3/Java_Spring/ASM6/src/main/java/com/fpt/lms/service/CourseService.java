package com.fpt.lms.service;

import com.fpt.lms.entity.*;
import com.fpt.lms.repository.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;

    public CourseService(CourseRepository courseRepository, CategoryRepository categoryRepository) {
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
    }

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
        List<Course> published = courseRepository.findByStatusOrderByCreatedAtDesc(2, Pageable.unpaged()).getContent();
        
        // Calculate frequencies using Java Streams
        Map<String, Long> freq = published.stream()
                .map(Course::getCategory)
                .filter(cat -> cat != null && !cat.isBlank())
                .flatMap(cat -> Arrays.stream(cat.split(",")))
                .map(String::trim)
                .filter(trimmed -> !trimmed.isEmpty())
                .collect(Collectors.groupingBy(name -> name, Collectors.counting()));

        List<Category> allCats = categoryRepository.findAll();
        Set<String> existingNames = new HashSet<>();

        // Update frequencies of existing categories or delete those that are no longer published
        allCats.forEach(cat -> {
            if (freq.containsKey(cat.getName())) {
                cat.setFrequency(freq.get(cat.getName()).intValue());
                categoryRepository.save(cat);
                existingNames.add(cat.getName());
            } else {
                categoryRepository.delete(cat);
            }
        });

        // Add newly published categories
        freq.entrySet().stream()
                .filter(entry -> !existingNames.contains(entry.getKey()))
                .map(entry -> {
                    Category cat = new Category();
                    cat.setName(entry.getKey());
                    cat.setFrequency(entry.getValue().intValue());
                    return cat;
                })
                .forEach(categoryRepository::save);
    }
}

