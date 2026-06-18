package com.example.demo.repository;

import com.example.demo.model.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

    List<Category> findAllByOrderByFrequencyDesc();
}
