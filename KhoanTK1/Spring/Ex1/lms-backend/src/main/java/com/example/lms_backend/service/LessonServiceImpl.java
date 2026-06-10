package com.example.lms_backend.service;

import com.example.lms_backend.entity.Lesson;
import com.example.lms_backend.entity.CourseId;
import com.example.lms_backend.repository.LessonRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LessonServiceImpl extends BaseServiceImpl<Lesson, Long> implements LessonService {

    private final LessonRepository lessonRepository;

    public LessonServiceImpl(LessonRepository repository) {
        super(repository);
        this.lessonRepository = repository;
    }

    @Override
    public List<Lesson> findByCourseId(CourseId courseId) {
        return lessonRepository.findByCourseId(courseId);
    }
}
