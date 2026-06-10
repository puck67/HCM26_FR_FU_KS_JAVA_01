package com.lms.coursemanager.service.impl;

import com.lms.coursemanager.dto.CourseDTO;
import com.lms.coursemanager.entity.Course;
import com.lms.coursemanager.entity.CourseId;
import com.lms.coursemanager.exception.DuplicateResourceException;
import com.lms.coursemanager.exception.ResourceNotFoundException;
import com.lms.coursemanager.repository.CourseRepository;
import com.lms.coursemanager.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    @Override
    @Transactional
    public CourseDTO createCourse(CourseDTO dto) {
        CourseId id = new CourseId(dto.getCourseCode(), dto.getStartDate());
        if (courseRepository.existsById(id)) {
            throw new DuplicateResourceException(
                String.format("Course with code '%s' starting on %s already exists.", 
                dto.getCourseCode(), dto.getStartDate())
            );
        }

        Course course = Course.builder()
                .id(id)
                .courseName(dto.getCourseName())
                .category(dto.getCategory())
                .instructor(dto.getInstructor())
                .build();

        Course saved = courseRepository.save(course);
        return mapToDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CourseDTO getCourse(String courseCode, LocalDate startDate) {
        CourseId id = new CourseId(courseCode, startDate);
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("Course not found with code '%s' and start date %s.", courseCode, startDate)
                ));
        return mapToDTO(course);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDTO> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(String courseCode, LocalDate startDate) {
        return courseRepository.existsById(new CourseId(courseCode, startDate));
    }

    private CourseDTO mapToDTO(Course course) {
        return CourseDTO.builder()
                .courseCode(course.getId().getCourseCode())
                .startDate(course.getId().getStartDate())
                .courseName(course.getCourseName())
                .category(course.getCategory())
                .instructor(course.getInstructor())
                .build();
    }
}
