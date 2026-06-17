package com.lms.repository;

import com.lms.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, String> {
    List<Category> findAllByOrderByFrequencyDesc();
}
