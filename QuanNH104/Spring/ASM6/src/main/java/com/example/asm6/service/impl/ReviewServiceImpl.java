package com.example.asm6.service.impl;

import com.example.asm6.model.Course;
import com.example.asm6.model.Review;
import com.example.asm6.repository.CourseRepository;
import com.example.asm6.repository.ReviewRepository;
import com.example.asm6.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public List<Review> getApprovedReviewsByCourse(Long courseId) {
        return reviewRepository.findByCourseIdAndStatusOrderByCreatedAtDesc(courseId, 2);
    }

    @Override
    @Transactional
    public Review createReview(Long courseId, Review review) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + courseId));
        review.setCourse(course);
        review.setStatus(1); // 1 = PENDING
        review.setCreatedAt(LocalDateTime.now());
        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getRecentApprovedReviews(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return reviewRepository.findRecentApprovedReviews(2, pageable);
    }

    @Override
    public Page<Review> getAllReviews(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return reviewRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Review approveReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Review not found with id: " + id));
        review.setStatus(2); // 2 = APPROVED
        return reviewRepository.save(review);
    }

    @Override
    @Transactional
    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Review not found with id: " + id));
        reviewRepository.delete(review);
    }
}
