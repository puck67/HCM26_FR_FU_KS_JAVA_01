package com.example.ASM6.repository;

import com.example.ASM6.model.Course;
import com.example.ASM6.model.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    // Find approved reviews for a specific course, ordered by creation date
    List<Review> findByCourseAndStatusOrderByCreatedAtDesc(Course course, Integer status);
    
    // Find recent approved reviews for display on public pages
    List<Review> findByStatusOrderByCreatedAtDesc(Integer status, Pageable pageable);
    
    // Find all reviews by status (e.g. pending or approved) for management
    List<Review> findByStatusOrderByCreatedAtDesc(Integer status);
    
    // Count reviews by status
    long countByStatus(Integer status);
    
    // Delete all reviews associated with a specific course
    void deleteByCourse(Course course);
}
