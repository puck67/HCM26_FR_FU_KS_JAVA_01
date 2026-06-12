package fa.training.lms.controllers;

import fa.training.lms.controllers.base.GenericController;
import fa.training.lms.dto.ApiResponse;
import fa.training.lms.entities.Course;
import fa.training.lms.entities.CourseId;
import fa.training.lms.interfaces.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/courses")
public class CourseController extends GenericController<Course, CourseId> {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        super(courseService);
        this.courseService = courseService;
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<Course>>> getByCategory(@PathVariable String category) {
        List<Course> data = courseService.getCoursesByCategory(category);
        return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách thành công", data));
    }

    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Course>> patch(@PathVariable CourseId id, @RequestBody Map<String, Object> updates) {
        Course data = courseService.patch(id, updates);
        return ResponseEntity.ok(new ApiResponse<>("Cập nhật thành công", data));
    }
}
