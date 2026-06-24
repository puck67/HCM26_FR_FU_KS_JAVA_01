package com.fsoft.training.courseshare.service;

import com.fsoft.training.courseshare.entity.Category;
import com.fsoft.training.courseshare.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Transactional
    public void incrementCategoryFrequency(String name) {
        String cleanName = name.trim();
        if (cleanName.isEmpty()) return;
        
        Category category = categoryRepository.findByName(cleanName).orElse(null);
        if (category == null) {
            category = new Category();
            category.setName(cleanName);
            category.setFrequency(1);
        } else {
            category.setFrequency(category.getFrequency() + 1);
        }
        categoryRepository.save(category);
    }

    @Transactional
    public void decrementCategoryFrequency(String name) {
        String cleanName = name.trim();
        if (cleanName.isEmpty()) return;

        Category category = categoryRepository.findByName(cleanName).orElse(null);
        if (category != null) {
            if (category.getFrequency() > 1) {
                category.setFrequency(category.getFrequency() - 1);
                categoryRepository.save(category);
            } else {
                categoryRepository.delete(category);
            }
        }
    }
}
