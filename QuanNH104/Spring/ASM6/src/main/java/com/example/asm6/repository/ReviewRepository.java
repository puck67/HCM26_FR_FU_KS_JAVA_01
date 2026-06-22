package com.example.asm6.repository;

import com.example.asm6.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    // Tìm các review theo trạng thái (ví dụ APPROVED)
    Page<Review> findByStatus(Integer status, Pageable pageable);
    
    // Tìm các review APPROVED của một khóa học cụ thể
    List<Review> findByCourseIdAndStatusOrderByCreatedAtDesc(Long courseId, Integer status);
    
    // Tìm các review gần đây nhất đã được duyệt
    @Query("SELECT r FROM Review r JOIN FETCH r.course c WHERE r.status = :status ORDER BY r.createdAt DESC")
    List<Review> findRecentApprovedReviews(@Param("status") Integer status, Pageable pageable);
}
