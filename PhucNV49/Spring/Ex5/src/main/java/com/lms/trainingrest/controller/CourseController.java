package com.lms.trainingrest.controller;

import com.lms.trainingrest.dto.CourseRequestDTO;
import com.lms.trainingrest.dto.CourseResponseDTO;
import com.lms.trainingrest.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@Tag(name = "Course Management", description = "Endpoints for course catalog management")
@SecurityRequirement(name = "bearerAuth")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    @Operation(summary = "Get all courses or retrieve a paginated page")
    public ResponseEntity<?> getAllCourses(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size) {
        if (page != null && size != null) {
            return ResponseEntity.ok(courseService.getAllCoursesPaginated(page, size));
        }
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get course details by ID")
    public ResponseEntity<CourseResponseDTO> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new course (ADMIN or TRAINER only)")
    public ResponseEntity<CourseResponseDTO> createCourse(@Valid @RequestBody CourseRequestDTO requestDTO) {
        CourseResponseDTO createdCourse = courseService.createCourse(requestDTO);
        return new ResponseEntity<>(createdCourse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update course details by ID (ADMIN or TRAINER only)")
    public ResponseEntity<CourseResponseDTO> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseRequestDTO requestDTO) {
        CourseResponseDTO updatedCourse = courseService.updateCourse(id, requestDTO);
        return ResponseEntity.ok(updatedCourse);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete course by ID (ADMIN only)")
    public ResponseEntity<UserController.MapResponse> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok(new UserController.MapResponse("Course deleted successfully"));
    }

    @GetMapping("/search")
    @Operation(summary = "Search courses by keyword matching name or description")
    public ResponseEntity<List<CourseResponseDTO>> searchCourses(@RequestParam("keyword") String keyword) {
        return ResponseEntity.ok(courseService.searchCourses(keyword));
    }
}
