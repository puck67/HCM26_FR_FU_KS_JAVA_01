package com.fresheracademy.lms.service;

import com.fresheracademy.lms.entity.Review;
import com.fresheracademy.lms.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public List<Review> getRecentApprovedReviews() {
        return reviewRepository.findTop10ByStatusOrderByCreatedAtDesc(2);
    }

    public Review saveReview(Review review) {
        review.setStatus(1); // Pending by default
        return reviewRepository.save(review);
    }

    public List<Review> getPendingReviews() {
        return reviewRepository.findByStatusOrderByCreatedAtDesc(1);
    }

    public List<Review> getApprovedReviews() {
        return reviewRepository.findByStatusOrderByCreatedAtDesc(2);
    }

    public void approveReview(Long id) {
        Review review = reviewRepository.findById(id).orElse(null);
        if (review != null) {
            review.setStatus(2);
            reviewRepository.save(review);
        }
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
}
