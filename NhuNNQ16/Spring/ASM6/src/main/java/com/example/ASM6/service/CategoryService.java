package com.example.ASM6.service;

import com.example.ASM6.model.Category;
import com.example.ASM6.model.Course;
import com.example.ASM6.repository.CategoryRepository;
import com.example.ASM6.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CourseRepository courseRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAllByOrderByFrequencyDesc();
    }

    @Transactional
    public void rebuildCategoryFrequencies() {
        // Count category occurrences across all PUBLISHED courses
        List<Course> courses = courseRepository.findAll();
        Map<String, Integer> categoryCounts = new HashMap<>();

        for (Course course : courses) {
            // Count categories for PUBLISHED courses only
            if (course.getStatus() == 2 && course.getCategory() != null) {
                String[] tags = course.getCategory().split(",");
                for (String tag : tags) {
                    String trimmed = tag.trim();
                    if (!trimmed.isEmpty()) {
                        categoryCounts.put(trimmed, categoryCounts.getOrDefault(trimmed, 0) + 1);
                    }
                }
            }
        }

        // Clear existing categories
        categoryRepository.deleteAllInBatch();

        // Save new categories
        for (Map.Entry<String, Integer> entry : categoryCounts.entrySet()) {
            categoryRepository.save(new Category(entry.getKey(), entry.getValue()));
        }
    }
}
