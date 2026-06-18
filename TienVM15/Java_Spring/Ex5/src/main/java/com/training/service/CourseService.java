package com.training.service;

import com.training.dto.CourseRequestDTO;
import com.training.dto.CourseResponseDTO;

import java.util.List;

public interface CourseService {
    List<CourseResponseDTO> getAllCourses();
    List<CourseResponseDTO> getAllCourses(int page, int size); // Bonus 3 (Pagination)
    CourseResponseDTO getCourseById(Long id);
    CourseResponseDTO createCourse(CourseRequestDTO request);
    CourseResponseDTO updateCourse(Long id, CourseRequestDTO request);
    void deleteCourse(Long id);
    List<CourseResponseDTO> searchCourses(String keyword); // Bonus 4 (Search)
}
