package com.lms.trainingrest.service.impl;

import com.lms.trainingrest.dto.CourseRequestDTO;
import com.lms.trainingrest.dto.CourseResponseDTO;
import com.lms.trainingrest.entity.Course;
import com.lms.trainingrest.exception.ResourceNotFoundException;
import com.lms.trainingrest.repository.CourseRepository;
import com.lms.trainingrest.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public List<CourseResponseDTO> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<CourseResponseDTO> getAllCoursesPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return courseRepository.findAll(pageable)
                .map(this::mapToDTO);
    }

    @Override
    public CourseResponseDTO getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        return mapToDTO(course);
    }

    @Override
    @Transactional
    public CourseResponseDTO createCourse(CourseRequestDTO requestDTO) {
        Course course = new Course()
                .setCourseName(requestDTO.getCourseName())
                .setDuration(requestDTO.getDuration())
                .setDescription(requestDTO.getDescription());

        Course savedCourse = courseRepository.save(course);
        return mapToDTO(savedCourse);
    }

    @Override
    @Transactional
    public CourseResponseDTO updateCourse(Long id, CourseRequestDTO requestDTO) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        course.setCourseName(requestDTO.getCourseName())
                .setDuration(requestDTO.getDuration())
                .setDescription(requestDTO.getDescription());

        Course updatedCourse = courseRepository.save(course);
        return mapToDTO(updatedCourse);
    }

    @Override
    @Transactional
    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        courseRepository.delete(course);
    }

    @Override
    public List<CourseResponseDTO> searchCourses(String keyword) {
        return courseRepository.searchCourses(keyword).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<CourseResponseDTO> searchCoursesPaginated(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return courseRepository.searchCoursesPaginated(keyword, pageable)
                .map(this::mapToDTO);
    }

    private CourseResponseDTO mapToDTO(Course course) {
        return new CourseResponseDTO()
                .setCourseId(course.getCourseId())
                .setCourseName(course.getCourseName())
                .setDuration(course.getDuration())
                .setDescription(course.getDescription());
    }
}
