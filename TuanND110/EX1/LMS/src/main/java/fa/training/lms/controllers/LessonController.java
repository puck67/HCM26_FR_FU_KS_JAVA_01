package fa.training.lms.controllers;

import fa.training.lms.controllers.base.GenericController;
import fa.training.lms.dto.ApiResponse;
import fa.training.lms.entities.Lesson;
import fa.training.lms.interfaces.LessonService;
import fa.training.lms.entities.CourseId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lessons")
public class LessonController extends GenericController<Lesson, Long> {
    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        super(lessonService);
        this.lessonService = lessonService;
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<List<Lesson>>> getByCourseId(@PathVariable CourseId courseId) {
        List<Lesson> data = lessonService.getLessonsByCourseId(courseId);
        return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách thành công", data));
    }

    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Lesson>> patch(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Lesson data = lessonService.patch(id, updates);
        return ResponseEntity.ok(new ApiResponse<>("Cập nhật thành công", data));
    }
}
