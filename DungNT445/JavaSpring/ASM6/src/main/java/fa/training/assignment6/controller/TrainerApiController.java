package fa.training.assignment6.controller;

import fa.training.assignment6.entity.LmsCourse;
import fa.training.assignment6.entity.CourseReview;
import fa.training.assignment6.repository.LmsCourseRepository;
import fa.training.assignment6.repository.CourseReviewRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/instructor")
public class TrainerApiController {
    private final LmsCourseRepository courseRepository;
    private final CourseReviewRepository reviewRepository;

    public TrainerApiController(LmsCourseRepository courseRepository, CourseReviewRepository reviewRepository) {
        this.courseRepository = courseRepository;
        this.reviewRepository = reviewRepository;
    }

    @PostMapping("/courses/{id}/status")
    public ResponseEntity<?> changeCourseStatus(@PathVariable Long id, @RequestBody Map<String, Integer> payload) {
        LmsCourse course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khóa học"));

        Integer newStatus = payload.get("status");
        course.setStatus(newStatus);
        courseRepository.save(course);

        return ResponseEntity.ok(Map.of("message", "Cập nhật trạng thái khóa học thành công!", "status", newStatus));
    }

    @PostMapping("/reviews/{id}/approve")
    public ResponseEntity<?> approveReview(@PathVariable Long id) {
        CourseReview review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đánh giá"));

        review.setStatus(2);
        reviewRepository.save(review);

        return ResponseEntity.ok(Map.of("message", "Đã phê duyệt đánh giá này!"));
    }

    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id) {
        CourseReview review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đánh giá"));

        reviewRepository.delete(review);
        return ResponseEntity.ok(Map.of("message", "Đã xóa đánh giá thành công!"));
    }
}
