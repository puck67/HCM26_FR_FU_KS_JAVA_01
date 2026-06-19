package com.fsoft.training.courseshare.repository;

import com.fsoft.training.courseshare.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Page<Course> findByStatusOrderByCreatedAtDesc(Integer status, Pageable pageable);
    Page<Course> findByStatusAndCategoryContainingIgnoreCaseOrderByCreatedAtDesc(Integer status, String category, Pageable pageable);
    
    long countByStatus(Integer status);
}
