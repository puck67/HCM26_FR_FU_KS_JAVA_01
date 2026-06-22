package com.example.ASM6.service;

import com.example.ASM6.model.Course;
import com.example.ASM6.model.Review;
import com.example.ASM6.repository.ReviewRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepo;

    public ReviewService(ReviewRepository reviewRepo) {
        this.reviewRepo = reviewRepo;
    }

    public List<Review> getApprovedReviewsForCourse(Course course) {
        return reviewRepo.findByCourseAndStatusOrderByCreatedAtDesc(course, 2);
    }

    public List<Review> getRecentApprovedReviews(int limit) {
        return reviewRepo.findByStatusOrderByCreatedAtDesc(2, PageRequest.of(0, limit));
    }

    public List<Review> getReviewsByStatus(Integer status) {
        return reviewRepo.findByStatusOrderByCreatedAtDesc(status);
    }

    public Optional<Review> getReviewById(Long id) {
        return reviewRepo.findById(id);
    }

    @Transactional
    public Review createReview(Review review) {
        review.setStatus(1);
        return reviewRepo.save(review);
    }

    @Transactional
    public void approveReview(Long id) {
        Optional<Review> reviewOpt = reviewRepo.findById(id);
        if (reviewOpt.isPresent()) {
            Review review = reviewOpt.get();
            review.setStatus(2);
            reviewRepo.save(review);
        }
    }

    @Transactional
    public void deleteReview(Long id) {
        reviewRepo.deleteById(id);
    }

    public long countReviewsByStatus(Integer status) {
        return reviewRepo.countByStatus(status);
    }

    public long countAllReviews() {
        return reviewRepo.count();
    }
}
