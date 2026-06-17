package com.lms.repository;

import com.lms.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseRepository extends JpaRepository<Course, Long> {
    
    Page<Course> findByStatusOrderByCreatedAtDesc(Integer status, Pageable pageable);

    @Query("SELECT c FROM Course c WHERE c.status = :status AND LOWER(c.category) LIKE LOWER(CONCAT('%', :category, '%')) ORDER BY c.createdAt DESC")
    Page<Course> findByStatusAndCategoryOrderByCreatedAtDesc(
            @Param("status") Integer status, 
            @Param("category") String category, 
            Pageable pageable
    );
}
