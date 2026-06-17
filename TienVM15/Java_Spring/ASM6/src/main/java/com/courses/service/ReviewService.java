package com.courses.service;

import com.courses.entity.Review;

import java.util.List;

public interface ReviewService {
    List<Review> getPendingReviews();
    List<Review> getApprovedReviews();
    List<Review> getRecentApprovedReviews();
    List<Review> getApprovedReviewsForCourse(Long courseId);
    Review saveReview(Review review);
    void approveReview(Long id);
    void deleteReview(Long id);
    long countPending();
}
