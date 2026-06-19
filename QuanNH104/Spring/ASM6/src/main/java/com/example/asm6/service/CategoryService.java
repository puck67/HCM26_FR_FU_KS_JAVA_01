package com.example.asm6.service;

import com.example.asm6.model.Category;
import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    List<Category> getActiveCategories();
    void recalculateCategoryFrequencies();
}
