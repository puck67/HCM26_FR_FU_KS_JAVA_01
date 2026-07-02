package com.fpt.lms.service;

import com.fpt.lms.entity.Category;
import com.fpt.lms.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAllByOrderByFrequencyDesc();
    }
}

