package fa.training.ex5.controller;

import fa.training.ex5.dto.request.CourseRequestDTO;
import fa.training.ex5.dto.response.ApiResponse;
import fa.training.ex5.dto.response.CourseResponse;
import fa.training.ex5.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER', 'STUDENT')")
    public ResponseEntity<ApiResponse<?>> getAllCourses(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        if (page != null && size != null) {
            Page<CourseResponse> coursePage = courseService.findAll(PageRequest.of(page, size));
            return ResponseEntity.ok(ApiResponse.success("Get paginated courses successful", coursePage));
        }
        List<CourseResponse> courses = courseService.findAll();
        return ResponseEntity.ok(ApiResponse.success("Get all courses successful", courses));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER', 'STUDENT')")
    public ResponseEntity<ApiResponse<CourseResponse>> getCourseById(@PathVariable Long id) {
        CourseResponse course = courseService.findById(id);
        return ResponseEntity.ok(ApiResponse.success("Get course by ID successful", course));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
    public ResponseEntity<ApiResponse<CourseResponse>> createCourse(
            @Valid @RequestBody CourseRequestDTO request) {
        CourseResponse createdCourse = courseService.createCourse(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Course created successfully", createdCourse));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
    public ResponseEntity<ApiResponse<CourseResponse>> updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody CourseRequestDTO request) {
        CourseResponse updatedCourse = courseService.updateCourse(id, request);
        return ResponseEntity.ok(ApiResponse.success("Course updated successfully", updatedCourse));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok(ApiResponse.success("Course deleted successfully", null));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER', 'STUDENT')")
    public ResponseEntity<ApiResponse<Page<CourseResponse>>> searchCourses(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<CourseResponse> coursePage = courseService.searchCourses(keyword, PageRequest.of(page, size));
        return ResponseEntity.ok(ApiResponse.success("Search courses successful", coursePage));
    }
}
