package com.example.training.service;

import com.example.training.dto.CourseRequestDTO;
import com.example.training.dto.CourseResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface CourseService {
    List<CourseResponseDTO> getAllCourses();
    Page<CourseResponseDTO> getAllCourses(Pageable pageable);
    CourseResponseDTO getCourseById(Long id);
    CourseResponseDTO createCourse(CourseRequestDTO request);
    CourseResponseDTO updateCourse(Long id, CourseRequestDTO request);
    void deleteCourse(Long id);
    List<CourseResponseDTO> searchCourses(String keyword);
}