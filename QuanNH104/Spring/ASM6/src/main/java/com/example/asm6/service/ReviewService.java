package com.example.asm6.service;

import com.example.asm6.model.Review;
import org.springframework.data.domain.Page;
import java.util.List;

public interface ReviewService {
    List<Review> getApprovedReviewsByCourse(Long courseId);
    Review createReview(Long courseId, Review review);
    List<Review> getRecentApprovedReviews(int limit);
    Page<Review> getAllReviews(int page, int size);
    Review approveReview(Long id);
    void deleteReview(Long id);
}
