package com.fsoft.training.courseshare.repository;

import com.fsoft.training.courseshare.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findTop5ByStatusOrderByCreatedAtDesc(Integer status);
    List<Review> findByCourseIdAndStatusOrderByCreatedAtDesc(Long courseId, Integer status);
    Page<Review> findByStatusOrderByCreatedAtDesc(Integer status, Pageable pageable);
    long countByStatus(Integer status);
}
