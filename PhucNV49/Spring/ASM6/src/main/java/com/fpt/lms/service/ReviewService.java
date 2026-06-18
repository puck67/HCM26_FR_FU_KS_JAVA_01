package com.fpt.lms.service;

import com.fpt.lms.entity.Review;
import com.fpt.lms.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class ReviewService {

    @Autowired private ReviewRepository reviewRepository;

    public List<Review> getApprovedReviews(Long courseId) {
        return reviewRepository.findByCourseIdAndStatus(courseId, 2);
    }

    public List<Review> getPendingReviews() {
        return reviewRepository.findByStatus(1);
    }

    public List<Review> getApprovedReviews() {
        return reviewRepository.findByStatus(2);
    }

    public List<Review> getRecentApprovedReviews() {
        return reviewRepository.findTop10ByStatusOrderByCreatedAtDesc(2);
    }

    public List<Review> getReviewsByCourse(Long courseId) {
        return reviewRepository.findByCourseId(courseId);
    }

    public Review save(Review review) {
        return reviewRepository.save(review);
    }

    public void approve(Long id) {
        reviewRepository.findById(id).ifPresent(r -> {
            r.setStatus(2);
            reviewRepository.save(r);
        });
    }

    public void delete(Long id) {
        reviewRepository.deleteById(id);
    }
}
