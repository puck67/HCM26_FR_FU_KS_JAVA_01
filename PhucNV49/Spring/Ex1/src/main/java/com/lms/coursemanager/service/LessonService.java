package com.lms.coursemanager.service;

import com.lms.coursemanager.dto.LessonDTO;

import java.time.LocalDate;
import java.util.List;

public interface LessonService {
    LessonDTO createLesson(String courseCode, LocalDate startDate, LessonDTO lessonDTO);
    LessonDTO updateLesson(Long id, LessonDTO lessonDTO);
    void deleteLesson(Long id);
    List<LessonDTO> getLessonsByCourse(String courseCode, LocalDate startDate);
}
