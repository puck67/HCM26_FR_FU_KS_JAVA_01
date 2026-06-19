package com.example.demo.service;

import com.example.demo.model.Review;
import com.example.demo.service.base.GenericService;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService extends GenericService<Review, Long> {

    Review saveReview(Review review);

    Review getReviewById(Long id);

    Review approveReview(Long id);

    void deleteReview(Long id);

    List<Review> getApprovedReviewsForCourse(Long courseId);

    List<Review> getRecentApprovedReviews();

    Page<Review> getReviewsByStatus(Integer status, Pageable pageable);

    Page<Review> getAllReviews(Pageable pageable);

    long countReviewsByStatus(Integer status);
}
