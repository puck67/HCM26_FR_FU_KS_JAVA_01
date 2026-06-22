package com.example.demo.repository;

import com.example.demo.model.Review;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByCourseIdAndStatusOrderByCreatedDateDesc(Long courseId, Integer status);

    List<Review> findTop5ByStatusOrderByCreatedDateDesc(Integer status);

    Page<Review> findByStatusOrderByCreatedDateDesc(Integer status, Pageable pageable);

    Page<Review> findAllByOrderByCreatedDateDesc(Pageable pageable);

    long countByStatus(Integer status);
}
