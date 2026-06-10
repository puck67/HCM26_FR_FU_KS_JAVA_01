package com.example.lms_backend.service;

import com.example.lms_backend.entity.Lesson;
import com.example.lms_backend.entity.CourseId;
import java.util.List;

public interface LessonService extends BaseService<Lesson, Long> {
    List<Lesson> findByCourseId(CourseId courseId);
}
