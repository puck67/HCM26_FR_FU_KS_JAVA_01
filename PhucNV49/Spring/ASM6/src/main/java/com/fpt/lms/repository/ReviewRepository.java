package com.fpt.lms.repository;

import com.fpt.lms.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByCourseIdAndStatus(Long courseId, Integer status);
    List<Review> findByStatus(Integer status);
    List<Review> findTop10ByStatusOrderByCreatedAtDesc(Integer status);
    List<Review> findByCourseId(Long courseId);
}
