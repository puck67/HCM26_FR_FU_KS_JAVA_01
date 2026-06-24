package com.example.demo.service;

import com.example.demo.model.Review;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.service.base.GenericServiceImpl;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ReviewServiceImpl extends GenericServiceImpl<Review, Long, ReviewRepository>
        implements ReviewService {

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        super(reviewRepository);
    }

    @Override
    @Transactional
    public Review saveReview(Review review) {
        if (review == null) {
            throw new IllegalArgumentException("Review cannot be null");
        }
        if (review.getCreatedDate() == null) {
            review.setCreatedDate(LocalDateTime.now());
        }
        if (review.getStatus() == null) {
            review.setStatus(1);
        }
        Review saved = save(review);
        log.info(new StringBuilder().append("Saved review for course ID: ")
                .append(review.getCourse().getId())
                .append(" by: ").append(review.getAuthorName()).toString());
        return saved;
    }

    @Override
    public Review getReviewById(Long id) {
        return findById(id);
    }

    @Override
    @Transactional
    public Review approveReview(Long id) {
        Review review = findById(id);
        review.setStatus(2);
        Review approved = save(review);
        log.info(new StringBuilder().append("Approved review ID: ").append(id).toString());
        return approved;
    }

    @Override
    @Transactional
    public void deleteReview(Long id) {
        deleteById(id);
        log.info(new StringBuilder().append("Deleted review ID: ").append(id).toString());
    }

    @Override
    public List<Review> getApprovedReviewsForCourse(Long courseId) {
        if (courseId == null) throw new IllegalArgumentException("Course ID cannot be null");
        return repository.findByCourseIdAndStatusOrderByCreatedDateDesc(courseId, 2);
    }

    @Override
    public List<Review> getRecentApprovedReviews() {
        return repository.findTop5ByStatusOrderByCreatedDateDesc(2);
    }

    @Override
    public Page<Review> getReviewsByStatus(Integer status, Pageable pageable) {
        return status == null
                ? repository.findAllByOrderByCreatedDateDesc(pageable)
                : repository.findByStatusOrderByCreatedDateDesc(status, pageable);
    }

    @Override
    public Page<Review> getAllReviews(Pageable pageable) {
        return repository.findAllByOrderByCreatedDateDesc(pageable);
    }

    @Override
    public long countReviewsByStatus(Integer status) {
        return status == null ? count() : repository.countByStatus(status);
    }
}
