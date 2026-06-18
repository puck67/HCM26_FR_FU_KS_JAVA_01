package com.lms.repository;

import com.lms.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByStatusOrderByCreatedAtDesc(Integer status);
    List<Review> findByCourseIdAndStatusOrderByCreatedAtDesc(Long courseId, Integer status);
    List<Review> findTop5ByStatusOrderByCreatedAtDesc(Integer status);
}
