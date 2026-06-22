package com.lms.coursemanager.service.impl;

import com.lms.coursemanager.dto.LessonDTO;
import com.lms.coursemanager.entity.Course;
import com.lms.coursemanager.entity.CourseId;
import com.lms.coursemanager.entity.Lesson;
import com.lms.coursemanager.exception.ResourceNotFoundException;
import com.lms.coursemanager.repository.CourseRepository;
import com.lms.coursemanager.repository.LessonRepository;
import com.lms.coursemanager.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;

    @Override
    @Transactional
    public LessonDTO createLesson(String courseCode, LocalDate startDate, LessonDTO dto) {
        CourseId courseId = new CourseId(courseCode, startDate);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("Course not found with code '%s' and start date %s.", courseCode, startDate)
                ));

        Lesson lesson = Lesson.builder()
                .lessonName(dto.getLessonName())
                .duration(dto.getDuration())
                .contentType(dto.getContentType())
                .status(dto.getStatus())
                .course(course)
                .build();

        Lesson saved = lessonRepository.save(lesson);
        return mapToDTO(saved);
    }

    @Override
    @Transactional
    public LessonDTO updateLesson(Long id, LessonDTO dto) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id " + id));

        lesson.setLessonName(dto.getLessonName());
        lesson.setDuration(dto.getDuration());
        lesson.setContentType(dto.getContentType());
        lesson.setStatus(dto.getStatus());

        Lesson saved = lessonRepository.save(lesson);
        return mapToDTO(saved);
    }

    @Override
    @Transactional
    public void deleteLesson(Long id) {
        if (!lessonRepository.existsById(id)) {
            throw new ResourceNotFoundException("Lesson not found with id " + id);
        }
        lessonRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonDTO> getLessonsByCourse(String courseCode, LocalDate startDate) {
        CourseId courseId = new CourseId(courseCode, startDate);
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException(
                String.format("Course not found with code '%s' and start date %s.", courseCode, startDate)
            );
        }
        return lessonRepository.findByCourseId(courseId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private LessonDTO mapToDTO(Lesson lesson) {
        return LessonDTO.builder()
                .id(lesson.getId())
                .lessonName(lesson.getLessonName())
                .duration(lesson.getDuration())
                .contentType(lesson.getContentType())
                .status(lesson.getStatus())
                .build();
    }
}
