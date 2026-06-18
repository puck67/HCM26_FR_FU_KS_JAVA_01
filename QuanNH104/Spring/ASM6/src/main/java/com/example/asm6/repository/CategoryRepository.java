package com.example.asm6.repository;

import com.example.asm6.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
    List<Category> findByFrequencyGreaterThanOrderByFrequencyDesc(Integer minFrequency);
    List<Category> findAllByOrderByFrequencyDesc();
}
