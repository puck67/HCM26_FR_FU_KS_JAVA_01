package com.fpt.lms.service;

import com.fpt.lms.entity.Review;
import com.fpt.lms.repository.ReviewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for managing Course Reviews.
 *
 * Improvements:
 * - Constructor injection (testable)
 * - SLF4J logging
 * - Returns unmodifiable lists via List.copyOf() where appropriate
 */
@Service
@Transactional
public class ReviewService {

    private static final Logger log = LoggerFactory.getLogger(ReviewService.class);

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    // ── Read Operations ────────────────────────────────────────────────────

    /**
     * Get approved reviews for a specific course.
     *
     * @param courseId the course ID
     * @return typed list of approved {@link Review} objects
     */
    @Transactional(readOnly = true)
    public List<Review> getApprovedReviews(Long courseId) {
        return reviewRepository.findByCourseIdAndStatus(courseId, 2);
    }

    /**
     * Get all pending reviews (status=1) across all courses.
     *
     * @return typed list of pending {@link Review} objects
     */
    @Transactional(readOnly = true)
    public List<Review> getPendingReviews() {
        return reviewRepository.findByStatus(1);
    }

    /**
     * Get all approved reviews (status=2) across all courses.
     *
     * @return typed list of approved {@link Review} objects
     */
    @Transactional(readOnly = true)
    public List<Review> getApprovedReviews() {
        return reviewRepository.findByStatus(2);
    }

    /**
     * Get the 10 most recently approved reviews (for homepage widget).
     *
     * @return typed list of up to 10 {@link Review} objects, newest first
     */
    @Transactional(readOnly = true)
    public List<Review> getRecentApprovedReviews() {
        return reviewRepository.findTop10ByStatusOrderByCreatedAtDesc(2);
    }

    /**
     * Get all reviews for a course regardless of status.
     *
     * @param courseId the course ID
     * @return typed list of all {@link Review} objects for the course
     */
    @Transactional(readOnly = true)
    public List<Review> getReviewsByCourse(Long courseId) {
        return reviewRepository.findByCourseId(courseId);
    }

    // ── Write Operations ───────────────────────────────────────────────────

    public Review save(Review review) {
        return reviewRepository.save(review);
    }

    /**
     * Approve a pending review (sets status from PENDING=1 to APPROVED=2).
     *
     * @param id the review ID
     */
    public void approve(Long id) {
        reviewRepository.findById(id).ifPresent(review -> {
            review.setStatus(2);
            reviewRepository.save(review);
            log.info("Review approved: id={}, author={}", review.getId(), review.getAuthorName());
        });
    }

    public void delete(Long id) {
        reviewRepository.deleteById(id);
        log.info("Review deleted: id={}", id);
    }
}
