package com.example.ASM6.repository;

import com.example.ASM6.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    
    // Get published courses with pagination (status 2 = PUBLISHED)
    Page<Course> findByStatusOrderByCreatedAtDesc(Integer status, Pageable pageable);
    
    // Find published courses containing a specific category
    @Query("SELECT c FROM Course c WHERE c.status = 2 AND LOWER(c.category) LIKE LOWER(CONCAT('%', :category, '%')) ORDER BY c.createdAt DESC")
    Page<Course> findPublishedByCategory(@Param("category") String category, Pageable pageable);
    
    // Find all courses for instructor, ordered by creation date
    List<Course> findAllByOrderByCreatedAtDesc();
    
    // Count courses by status
    long countByStatus(Integer status);
}
