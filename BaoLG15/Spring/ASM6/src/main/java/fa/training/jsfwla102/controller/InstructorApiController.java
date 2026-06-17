package fa.training.jsfwla102.controller;

import fa.training.jsfwla102.entity.Course;
import fa.training.jsfwla102.entity.Review;
import fa.training.jsfwla102.repository.CourseRepository;
import fa.training.jsfwla102.repository.ReviewRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/instructor")
public class InstructorApiController {
    private final CourseRepository courseRepository;
    private final ReviewRepository reviewRepository;

    public InstructorApiController(CourseRepository courseRepository, ReviewRepository reviewRepository) {
        this.courseRepository = courseRepository;
        this.reviewRepository = reviewRepository;
    }

    @PostMapping("/courses/{id}/status")
    public ResponseEntity<?> changeCourseStatus(@PathVariable Long id, @RequestBody Map<String, Integer> payload) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khóa học"));

        Integer newStatus = payload.get("status");
        course.setStatus(newStatus);
        courseRepository.save(course);

        return ResponseEntity.ok(Map.of("message", "Cập nhật trạng thái khóa học thành công!", "status", newStatus));
    }

    @PostMapping("/reviews/{id}/approve")
    public ResponseEntity<?> approveReview(@PathVariable Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đánh giá"));

        review.setStatus(2);
        reviewRepository.save(review);

        return ResponseEntity.ok(Map.of("message", "Đã phê duyệt đánh giá này!"));
    }

    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đánh giá"));

        reviewRepository.delete(review);
        return ResponseEntity.ok(Map.of("message", "Đã xóa đánh giá thành công!"));
    }
}
