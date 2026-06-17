package com.courses.service.impl;

import com.courses.entity.Review;
import com.courses.repository.ReviewRepository;
import com.courses.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Override
    public List<Review> getPendingReviews() {
        return reviewRepository.findByStatusOrderByIdDesc(1);
    }

    @Override
    public List<Review> getApprovedReviews() {
        return reviewRepository.findByStatusOrderByIdDesc(2);
    }

    @Override
    public List<Review> getRecentApprovedReviews() {
        return reviewRepository.findRecentApprovedReviews();
    }

    @Override
    public List<Review> getApprovedReviewsForCourse(Long courseId) {
        return reviewRepository.findByCourseIdAndStatusOrderByIdDesc(courseId, 2);
    }

    @Override
    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    public void approveReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));
        review.setStatus(2); // Set status to APPROVED
        reviewRepository.save(review);
    }

    @Override
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    @Override
    public long countPending() {
        return reviewRepository.findByStatusOrderByIdDesc(1).size();
    }
}
