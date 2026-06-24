package com.fsoft.training.courseshare.service;

import com.fsoft.training.courseshare.entity.Review;
import com.fsoft.training.courseshare.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    public static final int STATUS_PENDING = 1;
    public static final int STATUS_APPROVED = 2;

    @Autowired
    private ReviewRepository reviewRepository;

    public List<Review> getRecentApprovedReviews() {
        return reviewRepository.findTop5ByStatusOrderByCreatedAtDesc(STATUS_APPROVED);
    }

    public List<Review> getApprovedReviewsForCourse(Long courseId) {
        return reviewRepository.findByCourseIdAndStatusOrderByCreatedAtDesc(courseId, STATUS_APPROVED);
    }

    public Page<Review> getPendingReviews(Pageable pageable) {
        return reviewRepository.findByStatusOrderByCreatedAtDesc(STATUS_PENDING, pageable);
    }

    public Page<Review> getApprovedReviews(Pageable pageable) {
        return reviewRepository.findByStatusOrderByCreatedAtDesc(STATUS_APPROVED, pageable);
    }

    public Page<Review> getAllReviews(Pageable pageable) {
        return reviewRepository.findAll(pageable);
    }

    public void saveReview(Review review) {
        if (review.getStatus() == null) {
            review.setStatus(STATUS_PENDING);
        }
        reviewRepository.save(review);
    }

    public void approveReview(Long id) {
        Review review = reviewRepository.findById(id).orElse(null);
        if (review != null) {
            review.setStatus(STATUS_APPROVED);
            reviewRepository.save(review);
        }
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    public long getPendingReviewsCount() {
        return reviewRepository.countByStatus(STATUS_PENDING);
    }
}
