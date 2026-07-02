package com.fpt.lms.service;

import com.fpt.lms.entity.Course;
import com.fpt.lms.entity.Review;
import com.fpt.lms.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ReviewService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ReviewService Unit Tests")
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    private Course testCourse;
    private Review pendingReview;
    private Review approvedReview;

    @BeforeEach
    void setUp() {
        testCourse = Course.builder().id(1L).title("Spring Course").build();

        pendingReview = Review.builder()
            .id(1L)
            .course(testCourse)
            .authorName("Alice")
            .email("alice@example.com")
            .rating(5)
            .content("Great course!")
            .status(1) // PENDING
            .createdAt(LocalDateTime.now())
            .build();

        approvedReview = Review.builder()
            .id(2L)
            .course(testCourse)
            .authorName("Bob")
            .email("bob@example.com")
            .rating(4)
            .content("Very helpful content!")
            .status(2) // APPROVED
            .createdAt(LocalDateTime.now())
            .build();
    }

    @Test
    @DisplayName("getApprovedReviews - returns only approved reviews for a course")
    void getApprovedReviews_returnsByStatus() {
        when(reviewRepository.findByCourseIdAndStatus(1L, 2)).thenReturn(List.of(approvedReview));

        List<Review> result = reviewService.getApprovedReviews(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(2);
        verify(reviewRepository, times(1)).findByCourseIdAndStatus(1L, 2);
    }

    @Test
    @DisplayName("getPendingReviews - returns pending reviews")
    void getPendingReviews_returnsPending() {
        when(reviewRepository.findByStatus(1)).thenReturn(List.of(pendingReview));

        List<Review> result = reviewService.getPendingReviews();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(1);
    }

    @Test
    @DisplayName("approve - sets status to APPROVED and saves")
    void approve_setsStatusToApproved() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(pendingReview));
        when(reviewRepository.save(any(Review.class))).thenReturn(pendingReview);

        reviewService.approve(1L);

        assertThat(pendingReview.getStatus()).isEqualTo(2);
        verify(reviewRepository, times(1)).save(pendingReview);
    }

    @Test
    @DisplayName("approve - does nothing when review not found")
    void approve_doesNothingWhenNotFound() {
        when(reviewRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatNoException().isThrownBy(() -> reviewService.approve(99L));
        verify(reviewRepository, never()).save(any());
    }

    @Test
    @DisplayName("delete - calls deleteById")
    void delete_callsDeleteById() {
        reviewService.delete(1L);
        verify(reviewRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("save - persists review and returns saved")
    void save_persistsAndReturns() {
        when(reviewRepository.save(any(Review.class))).thenReturn(pendingReview);

        Review saved = reviewService.save(pendingReview);

        assertThat(saved).isEqualTo(pendingReview);
        verify(reviewRepository, times(1)).save(pendingReview);
    }

    @Test
    @DisplayName("getRecentApprovedReviews - returns top 10 approved")
    void getRecentApprovedReviews_returnsTop10() {
        when(reviewRepository.findTop10ByStatusOrderByCreatedAtDesc(2))
            .thenReturn(List.of(approvedReview));

        List<Review> result = reviewService.getRecentApprovedReviews();

        assertThat(result).hasSize(1);
        verify(reviewRepository, times(1)).findTop10ByStatusOrderByCreatedAtDesc(2);
    }
}
