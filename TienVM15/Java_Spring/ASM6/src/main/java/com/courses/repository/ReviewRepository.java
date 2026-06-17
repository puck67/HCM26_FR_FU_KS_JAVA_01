package com.courses.repository;

import com.courses.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    // Find all reviews by status, sorted by id desc
    List<Review> findByStatusOrderByIdDesc(int status);

    // Find reviews for a course by status
    List<Review> findByCourseIdAndStatusOrderByIdDesc(Long courseId, int status);

    // Find most recent approved reviews
    @Query("SELECT r FROM Review r WHERE r.status = 2 ORDER BY r.id DESC")
    List<Review> findRecentApprovedReviews();
}
