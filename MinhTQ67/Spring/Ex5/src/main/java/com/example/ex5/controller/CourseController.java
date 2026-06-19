package com.example.ex5.controller;

import com.example.ex5.dto.CourseRequestDTO;
import com.example.ex5.dto.CourseResponseDTO;
import com.example.ex5.entity.Course;
import com.example.ex5.repository.CourseRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/courses")
@Tag(name = "Course Management API", description = "Các API quản lý khóa học")
@SecurityRequirement(name = "bearerAuth")
public class CourseController {

    private final CourseRepository courseRepository;

    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping
    @Operation(summary = "Lấy danh sách khóa học (Hỗ trợ phân trang)")
    public ResponseEntity<?> getAllCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable paging = PageRequest.of(page, size);
        Page<Course> pageTuts = courseRepository.findAll(paging);
        List<CourseResponseDTO> dtos = pageTuts.getContent().stream().map(this::mapToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy chi tiết khóa học theo ID")
    public ResponseEntity<CourseResponseDTO> getCourseById(@PathVariable Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return ResponseEntity.ok(mapToDTO(course));
    }

    @GetMapping("/search")
    @Operation(summary = "Tìm kiếm khóa học theo tên (Hỗ trợ phân trang)")
    public ResponseEntity<List<CourseResponseDTO>> searchCourses(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable paging = PageRequest.of(page, size);
        Page<Course> pageTuts = courseRepository.findByCourseNameContainingIgnoreCase(keyword, paging);
        List<CourseResponseDTO> dtos = pageTuts.getContent().stream().map(this::mapToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    @Operation(summary = "Tạo mới khóa học (Chỉ ADMIN, TRAINER)")
    public ResponseEntity<CourseResponseDTO> createCourse(@Valid @RequestBody CourseRequestDTO courseRequestDTO) {
        Course course = new Course();
        course.setCourseName(courseRequestDTO.getCourseName());
        course.setDuration(courseRequestDTO.getDuration());
        course.setDescription(courseRequestDTO.getDescription());

        Course savedCourse = courseRepository.save(course);
        return ResponseEntity.ok(mapToDTO(savedCourse));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật khóa học (Chỉ ADMIN, TRAINER)")
    public ResponseEntity<CourseResponseDTO> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseRequestDTO courseRequestDTO) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        course.setCourseName(courseRequestDTO.getCourseName());
        course.setDuration(courseRequestDTO.getDuration());
        course.setDescription(courseRequestDTO.getDescription());

        Course updatedCourse = courseRepository.save(course);
        return ResponseEntity.ok(mapToDTO(updatedCourse));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa khóa học (Chỉ ADMIN)")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        if (!courseRepository.existsById(id)) {
            throw new RuntimeException("Course not found");
        }
        courseRepository.deleteById(id);
        return ResponseEntity.ok("{\"message\": \"Course deleted successfully!\"}");
    }

    private CourseResponseDTO mapToDTO(Course course) {
        CourseResponseDTO dto = new CourseResponseDTO();
        dto.setId(course.getCourseId());
        dto.setCourseName(course.getCourseName());
        dto.setDuration(course.getDuration());
        dto.setDescription(course.getDescription());
        return dto;
    }
}
