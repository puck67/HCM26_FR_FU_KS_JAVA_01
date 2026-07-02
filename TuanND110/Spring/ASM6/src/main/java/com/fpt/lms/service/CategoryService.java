package com.fpt.lms.service;

import com.fpt.lms.entity.Category;
import com.fpt.lms.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing Course Categories.
 * Categories are auto-rebuilt from published course data via {@link CourseService#rebuildCategoryStats()}.
 */
@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Returns all categories ordered by frequency descending.
     *
     * @return typed list of {@link Category}
     */
    public List<Category> getAllCategories() {
        return categoryRepository.findAllByOrderByFrequencyDesc();
    }

    /**
     * Find category by exact name.
     *
     * @param name category name
     * @return Optional containing category if found
     */
    public Optional<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }
}
