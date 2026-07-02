package com.lms.service;

import com.lms.model.Course;
import com.lms.model.Lesson;
import com.lms.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    public List<Lesson> findByCourse(String courseCode, LocalDate startDate) {
        return lessonRepository.findByCourse_CourseCodeAndCourse_StartDate(courseCode, startDate);
    }

    public Optional<Lesson> findById(Integer id) {
        return lessonRepository.findById(id);
    }

    public void save(Lesson lesson) {
        lessonRepository.save(lesson);
    }

    public void delete(Integer id) {
        lessonRepository.deleteById(id);
    }
}
