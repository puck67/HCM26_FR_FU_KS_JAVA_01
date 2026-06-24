package com.example.ASM6.service;

import com.example.ASM6.model.Course;
import com.example.ASM6.model.Review;
import com.example.ASM6.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public List<Review> getApprovedReviewsForCourse(Course course) {
        return reviewRepository.findByCourseAndStatusOrderByCreatedAtDesc(course, 2); // 2 = APPROVED
    }

    public List<Review> getRecentApprovedReviews(int limit) {
        return reviewRepository.findByStatusOrderByCreatedAtDesc(2, PageRequest.of(0, limit)); // 2 = APPROVED
    }

    public List<Review> getReviewsByStatus(Integer status) {
        return reviewRepository.findByStatusOrderByCreatedAtDesc(status);
    }

    public Optional<Review> getReviewById(Long id) {
        return reviewRepository.findById(id);
    }

    @Transactional
    public Review createReview(Review review) {
        review.setStatus(1); // 1 = PENDING by default
        return reviewRepository.save(review);
    }

    @Transactional
    public void approveReview(Long id) {
        reviewRepository.findById(id).ifPresent(review -> {
            review.setStatus(2); // 2 = APPROVED
            reviewRepository.save(review);
        });
    }

    @Transactional
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    public long countReviewsByStatus(Integer status) {
        return reviewRepository.countByStatus(status);
    }

    public long countAllReviews() {
        return reviewRepository.count();
    }
}
