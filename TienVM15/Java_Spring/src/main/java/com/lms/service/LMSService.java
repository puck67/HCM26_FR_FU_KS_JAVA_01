package com.lms.service;

import com.lms.entity.Course;
import com.lms.entity.CourseId;
import com.lms.entity.Lesson;
import com.lms.repository.CourseRepository;
import com.lms.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class LMSService {

    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;

    // Course Methods
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(String courseCode, LocalDate startDate) {
        return courseRepository.findById(new CourseId(courseCode, startDate));
    }

    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    public boolean courseExists(String courseCode, LocalDate startDate) {
        return courseRepository.existsById(new CourseId(courseCode, startDate));
    }

    // Lesson Methods
    public List<Lesson> getLessonsByCourse(String courseCode, LocalDate startDate) {
        return lessonRepository.findByCourseCourseCodeAndCourseStartDate(courseCode, startDate);
    }

    public Optional<Lesson> getLessonById(Long id) {
        return lessonRepository.findById(id);
    }

    public Lesson saveLesson(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    public void deleteLesson(Long id) {
        lessonRepository.deleteById(id);
    }
}
