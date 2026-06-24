package com.lms.controller;

import com.lms.controller.base.GenericController;
import com.lms.model.Course;
import com.lms.model.CourseId;
import com.lms.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/courses")
public class CourseController extends GenericController<Course, CourseId, CourseService> {

    @Override
    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid Course course) {
        CourseId courseId = new CourseId(course.getCourseCode(), course.getStartDate());
        if (service.existsById(courseId)) {
            return ResponseEntity.badRequest().body("Course with this Code and Start Date already exists!");
        }
        Course saved = service.save(course);
        return ResponseEntity.ok(saved);
    }
}
