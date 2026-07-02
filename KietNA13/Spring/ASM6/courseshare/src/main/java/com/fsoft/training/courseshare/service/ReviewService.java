package com.fsoft.training.courseshare.service;

import com.fsoft.training.courseshare.entity.Review;
import com.fsoft.training.courseshare.repository.ReviewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private static final Logger log = LoggerFactory.getLogger(ReviewService.class);

    public static final int STATUS_PENDING = 1;
    public static final int STATUS_APPROVED = 2;

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> fetchRecentApprovedReviews() {
        log.debug("Fetching recent approved reviews");
        return reviewRepository.findTop5ByStatusOrderByCreatedAtDesc(STATUS_APPROVED);
    }

    public List<Review> fetchApprovedReviewsByCourse(Long courseId) {
        log.debug("Fetching approved reviews for course: {}", courseId);
        return reviewRepository.findByCourseIdAndStatusOrderByCreatedAtDesc(courseId, STATUS_APPROVED);
    }

    public Page<Review> fetchPendingReviews(Pageable pageable) {
        log.debug("Fetching pending reviews page: {}", pageable);
        return reviewRepository.findByStatusOrderByCreatedAtDesc(STATUS_PENDING, pageable);
    }

    public Page<Review> fetchApprovedReviews(Pageable pageable) {
        log.debug("Fetching approved reviews page: {}", pageable);
        return reviewRepository.findByStatusOrderByCreatedAtDesc(STATUS_APPROVED, pageable);
    }

    public Page<Review> fetchAllReviews(Pageable pageable) {
        log.debug("Fetching all reviews page: {}", pageable);
        return reviewRepository.findAll(pageable);
    }

    public void saveReviewEntity(Review review) {
        log.info("Saving review for course: {}", review.getCourse() != null ? review.getCourse().getId() : "null");
        if (review.getStatus() == null) {
            review.setStatus(STATUS_PENDING);
        }
        reviewRepository.save(review);
    }

    public void approveReviewById(Long id) {
        log.info("Approving review id: {}", id);
        Review review = reviewRepository.findById(id).orElse(null);
        if (review != null) {
            review.setStatus(STATUS_APPROVED);
            reviewRepository.save(review);
            log.info("Review approved successfully: {}", id);
        } else {
            log.warn("Review with id {} not found for approval", id);
        }
    }

    public void deleteReviewById(Long id) {
        log.info("Deleting review id: {}", id);
        reviewRepository.deleteById(id);
    }

    public long countPendingReviews() {
        return reviewRepository.countByStatus(STATUS_PENDING);
    }
}
