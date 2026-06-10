package fa.training.lms.controllers;

import fa.training.lms.controllers.base.GenericController;
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
    public ResponseEntity<List<Course>> getByCategory(@PathVariable String category) {
        return ResponseEntity.ok(courseService.getCoursesByCategory(category));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Course> patch(@PathVariable CourseId id, @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(courseService.patch(id, updates));
    }
}
