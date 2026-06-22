package com.lms.trainingrest.service;

import com.lms.trainingrest.dto.CourseRequestDTO;
import com.lms.trainingrest.dto.CourseResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CourseService {
    List<CourseResponseDTO> getAllCourses();
    Page<CourseResponseDTO> getAllCoursesPaginated(int page, int size);
    CourseResponseDTO getCourseById(Long id);
    CourseResponseDTO createCourse(CourseRequestDTO requestDTO);
    CourseResponseDTO updateCourse(Long id, CourseRequestDTO requestDTO);
    void deleteCourse(Long id);
    List<CourseResponseDTO> searchCourses(String keyword);
    Page<CourseResponseDTO> searchCoursesPaginated(String keyword, int page, int size);
}
