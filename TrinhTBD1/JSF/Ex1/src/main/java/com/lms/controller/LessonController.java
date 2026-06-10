package com.lms.controller;

import com.lms.controller.base.GenericController;
import com.lms.model.Course;
import com.lms.model.Lesson;
import com.lms.service.LessonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/lessons")
public class LessonController extends GenericController<Lesson, Long, LessonService> {

    @GetMapping("/course")
    public ResponseEntity<?> getLessonsByCourse(@RequestParam String courseCode, @RequestParam String startDate) {
        try {
            LocalDate date = LocalDate.parse(startDate);
            Course dummyCourse = new Course();
            dummyCourse.setCourseCode(courseCode);
            dummyCourse.setStartDate(date);
            return ResponseEntity.ok(service.getLessonsForCourse(dummyCourse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid parameters: " + e.getMessage());
        }
    }
}
