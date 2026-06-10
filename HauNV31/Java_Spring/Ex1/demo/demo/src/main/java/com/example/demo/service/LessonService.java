package com.example.demo.service;

import com.example.demo.model.Course;
import com.example.demo.model.Lesson;
import com.example.demo.repository.LessonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LessonService {

    private final LessonRepository lessonRepository;

    public LessonService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    public Lesson saveLesson(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    public List<Lesson> getLessonsByCourse(String courseCode, LocalDate startDate) {
        return lessonRepository.findByCourseIdCourseCodeAndCourseIdStartDateOrderByIdAsc(courseCode, startDate);
    }
    

    public Optional<Lesson> getLessonById(Long id) {
        return lessonRepository.findById(id);
    }

    public void deleteLesson(Long id) {
        lessonRepository.deleteById(id);
    }

    public Lesson updateLesson(Long id, String lessonName, Integer duration,
                                com.example.demo.model.ContentType contentType,
                                com.example.demo.model.LessonStatus status) {
        Optional<Lesson> optional = lessonRepository.findById(id);
        if (optional.isPresent()) {
            Lesson lesson = optional.get();
            lesson.setLessonName(lessonName);
            lesson.setDuration(duration);
            lesson.setContentType(contentType);
            lesson.setStatus(status);
            return lessonRepository.save(lesson);
        }
        return null;
    }
}
