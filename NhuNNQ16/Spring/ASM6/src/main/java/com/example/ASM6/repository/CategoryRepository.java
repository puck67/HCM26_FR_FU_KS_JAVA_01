package com.example.ASM6.repository;

import com.example.ASM6.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    
    // Get all categories ordered by frequency (most used first)
    List<Category> findAllByOrderByFrequencyDesc();
}
