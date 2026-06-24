package com.example.menu.service;

import com.example.menu.entity.Course;
import com.example.menu.entity.Lesson;
import com.example.menu.repository.LessonRepository;
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
