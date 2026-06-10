package fa.training.lms.controllers;

import fa.training.lms.controllers.base.GenericController;
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
    public ResponseEntity<List<Lesson>> getByCourseId(@PathVariable CourseId courseId) {
        return ResponseEntity.ok(lessonService.getLessonsByCourseId(courseId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Lesson> patch(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(lessonService.patch(id, updates));
    }
}
