package com.fresheracademy.lms.repository;

import com.fresheracademy.lms.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findTop10ByStatusOrderByCreatedAtDesc(Integer status);
    List<Review> findByStatusOrderByCreatedAtDesc(Integer status);
}
