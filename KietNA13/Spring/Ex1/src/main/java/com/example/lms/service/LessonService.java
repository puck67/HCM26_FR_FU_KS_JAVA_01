package com.example.lms.service;

import com.example.lms.entity.Course;
import com.example.lms.entity.Lesson;
import com.example.lms.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;

    public Lesson saveLesson(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    public List<Lesson> getLessonsByCourse(Course course) {
        return lessonRepository.findByCourse(course);
    }

    public Optional<Lesson> getLessonById(Long id) {
        return lessonRepository.findById(id);
    }

    public void deleteLesson(Long id) {
        lessonRepository.deleteById(id);
    }
}
