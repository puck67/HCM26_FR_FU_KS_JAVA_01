package com.lms.coursemanager.controller;

import com.lms.coursemanager.dto.ApiResponse;
import com.lms.coursemanager.dto.CourseDTO;
import com.lms.coursemanager.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<ApiResponse<CourseDTO>> createCourse(@Valid @RequestBody CourseDTO courseDTO) {
        CourseDTO created = courseService.createCourse(courseDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Add a new Course successfully!", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CourseDTO>>> getAllCourses() {
        List<CourseDTO> courses = courseService.getAllCourses();
        return ResponseEntity.ok(ApiResponse.success("Courses retrieved successfully", courses));
    }

    @GetMapping("/detail")
    public ResponseEntity<ApiResponse<CourseDTO>> getCourseDetail(
            @RequestParam String courseCode,
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate startDate) {
        CourseDTO course = courseService.getCourse(courseCode, startDate);
        return ResponseEntity.ok(ApiResponse.success("Course details retrieved successfully", course));
    }
}
