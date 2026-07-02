package com.fsoft.training.courseshare.service;

import com.fsoft.training.courseshare.entity.Category;
import com.fsoft.training.courseshare.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private static final Logger log = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> fetchAllCategories() {
        log.debug("Fetching all categories");
        return categoryRepository.findAll();
    }

    @Transactional
    public void addCategoryFrequency(String name) {
        String cleanName = name.trim();
        if (cleanName.isEmpty()) return;
        
        log.info("Incrementing frequency for category: {}", cleanName);
        try {
            Category category = categoryRepository.findByName(cleanName)
                    .orElseGet(() -> {
                        Category newCat = new Category();
                        newCat.setName(cleanName);
                        newCat.setFrequency(0);
                        return newCat;
                    });
            category.setFrequency(category.getFrequency() + 1);
            categoryRepository.save(category);
        } catch (Exception e) {
            log.error("Failed to increment category frequency for: {}", cleanName, e);
        }
    }

    @Transactional
    public void removeCategoryFrequency(String name) {
        String cleanName = name.trim();
        if (cleanName.isEmpty()) return;

        log.info("Decrementing frequency for category: {}", cleanName);
        try {
            categoryRepository.findByName(cleanName).ifPresent(category -> {
                if (category.getFrequency() > 1) {
                    category.setFrequency(category.getFrequency() - 1);
                    categoryRepository.save(category);
                } else {
                    categoryRepository.delete(category);
                }
            });
        } catch (Exception e) {
            log.error("Failed to decrement category frequency for: {}", cleanName, e);
        }
    }
}
