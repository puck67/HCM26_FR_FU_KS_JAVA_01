package com.lms.coursemanager.controller;

import com.lms.coursemanager.dto.ApiResponse;
import com.lms.coursemanager.dto.LessonDTO;
import com.lms.coursemanager.service.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LessonController {

    private final LessonService lessonService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<LessonDTO>>> getLessons(
            @RequestParam String courseCode,
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate startDate) {
        List<LessonDTO> lessons = lessonService.getLessonsByCourse(courseCode, startDate);
        return ResponseEntity.ok(ApiResponse.success("Lessons retrieved successfully", lessons));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LessonDTO>> createLesson(
            @RequestParam String courseCode,
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate startDate,
            @Valid @RequestBody LessonDTO lessonDTO) {
        LessonDTO created = lessonService.createLesson(courseCode, startDate, lessonDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Add a new Lesson successfully!", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LessonDTO>> updateLesson(
            @PathVariable Long id,
            @Valid @RequestBody LessonDTO lessonDTO) {
        LessonDTO updated = lessonService.updateLesson(id, lessonDTO);
        return ResponseEntity.ok(ApiResponse.success("Lesson updated successfully!", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);
        return ResponseEntity.ok(ApiResponse.success("Lesson deleted successfully!"));
    }
}
