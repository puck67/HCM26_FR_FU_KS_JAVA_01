package com.example.demo.service;

import com.example.demo.model.Course;
import com.example.demo.model.CourseId;
import com.example.demo.model.Lesson;
import com.example.demo.repository.LessonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LessonService {
    private final CourseService courseService;
    private final LessonRepository lessonRepository;

    public LessonService(CourseService courseService, LessonRepository lessonRepository) {
        this.courseService = courseService;
        this.lessonRepository = lessonRepository;
    }

    public List<Lesson> findByCourse(CourseId courseId) {
        return lessonRepository.findByCourseIdOrderByIdAsc(courseId);
    }

    public Lesson getForEdit(Long lessonId) {
        return lessonRepository.findById(lessonId).orElse(new Lesson());
    }

    @Transactional
    public void save(CourseId courseId, Lesson lesson) {
        Course course = courseService.getRequired(courseId);
        lesson.setCourse(course);
        lessonRepository.save(lesson);
    }

    public void delete(Long lessonId) {
        lessonRepository.deleteById(lessonId);
    }
}
