package com.example.EX1.service;

import com.example.EX1.model.CourseId;
import com.example.EX1.model.Lesson;
import com.example.EX1.repository.LessonRepository;
import com.example.EX1.service.base.GenericServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * LessonService - kế thừa GenericServiceImpl với Entity=Lesson, ID=Long.
 * Bổ sung thêm hàm tìm kiếm theo CourseId (đặc thù của Lesson).
 */
@Service
public class LessonService extends GenericServiceImpl<Lesson, Long> {

    private final LessonRepository lessonRepository;

    public LessonService(LessonRepository repository) {
        super(repository);
        this.lessonRepository = repository;
    }

    /**
     * Hàm đặc thù: lấy danh sách Lesson theo CourseId.
     */
    public List<Lesson> findByCourseId(CourseId courseId) {
        return lessonRepository.findByCourseId(courseId);
    }
}
