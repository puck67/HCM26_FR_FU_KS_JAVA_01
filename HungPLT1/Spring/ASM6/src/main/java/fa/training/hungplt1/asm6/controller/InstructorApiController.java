package fa.training.hungplt1.asm6.controller;

import fa.training.hungplt1.asm6.entity.Course;
import fa.training.hungplt1.asm6.entity.Review;
import fa.training.hungplt1.asm6.repository.CourseRepository;
import fa.training.hungplt1.asm6.repository.ReviewRepository;
import fa.training.hungplt1.asm6.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/instructor")
public class InstructorApiController {
    private final CourseRepository courseRepository;
    private final ReviewRepository reviewRepository;
    private final CourseService courseService;

    public InstructorApiController(CourseRepository courseRepository, ReviewRepository reviewRepository, CourseService courseService) {
        this.courseRepository = courseRepository;
        this.reviewRepository = reviewRepository;
        this.courseService = courseService;
    }

    @PostMapping("/courses/{id}/status")
    public ResponseEntity<?> changeCourseStatus(@PathVariable Long id, @RequestBody Map<String, Integer> payload) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khóa học"));

        Integer newStatus = payload.get("status");
        course.setStatus(newStatus);
        courseRepository.save(course);

        return ResponseEntity.ok(Map.of("message", "Cập nhật trạng thái thành công!", "status", newStatus));
    }

    @DeleteMapping("/courses/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok(Map.of("message", "Đã xóa khóa học thành công!"));
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
