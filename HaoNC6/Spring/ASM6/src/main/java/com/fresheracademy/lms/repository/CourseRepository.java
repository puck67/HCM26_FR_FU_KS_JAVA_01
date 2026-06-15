package com.fresheracademy.lms.repository;

import com.fresheracademy.lms.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Page<Course> findByStatusOrderByCreatedAtDesc(Integer status, Pageable pageable);
    List<Course> findByStatusAndCategoryContainingOrderByCreatedAtDesc(Integer status, String category);
    Page<Course> findByStatusAndCategoryContainingOrderByCreatedAtDesc(Integer status, String category, Pageable pageable);
}
