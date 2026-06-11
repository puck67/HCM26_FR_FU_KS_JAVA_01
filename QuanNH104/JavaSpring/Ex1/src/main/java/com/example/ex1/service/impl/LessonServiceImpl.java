package com.example.ex1.service.impl;

import com.example.ex1.entity.Lesson;
import com.example.ex1.repository.LessonRepository;
import com.example.ex1.service.LessonService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;

    // Constructor Injection thủ công
    public LessonServiceImpl(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    @Override
    public List<Lesson> findByCourse(String courseCode, LocalDate startDate) {
        return lessonRepository.findByCourseIdCourseCodeAndCourseIdStartDate(courseCode, startDate);
    }

    @Override
    public Optional<Lesson> findById(Long id) {
        return lessonRepository.findById(id);
    }

    @Override
    public Lesson save(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    @Override
    public void deleteById(Long id) {
        lessonRepository.deleteById(id);
    }
}
