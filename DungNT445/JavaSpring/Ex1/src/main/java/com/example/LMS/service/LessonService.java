package com.example.LMS.service;

import com.example.LMS.entity.Course;
import com.example.LMS.entity.Lesson;
import com.example.LMS.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LessonService {

    private final LessonRepository lessonRepository;

    @Autowired
    public LessonService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    public Lesson saveLesson(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    public Optional<Lesson> getLessonById(Long id) {
        return lessonRepository.findById(id);
    }

    public List<Lesson> getLessonsByCourse(Course course) {
        return lessonRepository.findByCourseOrderByLessonNameAsc(course);
    }

    public void deleteLesson(Long id) {
        lessonRepository.deleteById(id);
    }
}
