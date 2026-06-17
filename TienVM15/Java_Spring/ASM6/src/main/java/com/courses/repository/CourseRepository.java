package com.courses.repository;

import com.courses.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    
    // Find all published courses with pagination, sorting by ID desc
    Page<Course> findByStatusOrderByIdDesc(int status, Pageable pageable);

    // Find all courses by category tag
    @Query("SELECT c FROM Course c WHERE c.status = :status AND LOWER(c.category) LIKE LOWER(CONCAT('%', :category, '%')) ORDER BY c.id DESC")
    Page<Course> findByStatusAndCategoryLike(@Param("status") int status, @Param("category") String category, Pageable pageable);

    // List all courses (all statuses) sorted
    List<Course> findAllByOrderByIdDesc();
}
