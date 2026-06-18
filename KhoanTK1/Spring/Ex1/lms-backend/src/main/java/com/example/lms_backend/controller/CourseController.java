package com.example.lms_backend.controller;

import com.example.lms_backend.entity.Course;
import com.example.lms_backend.entity.CourseId;
import com.example.lms_backend.entity.Lesson;
import com.example.lms_backend.service.CourseService;
import com.example.lms_backend.service.LessonService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController extends BaseController<Course, CourseId> {

    private final LessonService lessonService;

    public CourseController(CourseService courseService, LessonService lessonService) {
        super(courseService);
        this.lessonService = lessonService;
    }

    @GetMapping("/{courseCode}/{startDate}")
    public ResponseEntity<Course> getByCompositeId(
            @PathVariable String courseCode,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        CourseId id = new CourseId(courseCode, startDate);
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{courseCode}/{startDate}")
    public ResponseEntity<Course> updateByCompositeId(
            @PathVariable String courseCode,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestBody Course course) {
        CourseId id = new CourseId(courseCode, startDate);
        try {
            return ResponseEntity.ok(service.update(id, course));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{courseCode}/{startDate}")
    public ResponseEntity<Void> deleteByCompositeId(
            @PathVariable String courseCode,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        CourseId id = new CourseId(courseCode, startDate);
        try {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{courseCode}/{startDate}/lessons")
    public ResponseEntity<List<Lesson>> getLessonsForCourse(
            @PathVariable String courseCode,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        CourseId id = new CourseId(courseCode, startDate);
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(lessonService.findByCourseId(id));
    }
}
