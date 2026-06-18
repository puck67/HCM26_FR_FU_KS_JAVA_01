package com.lms.service.impl;

import com.lms.entity.Course;
import com.lms.entity.CourseId;
import com.lms.entity.Lesson;
import com.lms.repository.CourseRepository;
import com.lms.repository.LessonRepository;
import com.lms.service.LMSService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class LMSServiceImpl implements LMSService {

    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Optional<Course> getCourseById(String courseCode, LocalDate startDate) {
        return courseRepository.findById(new CourseId(courseCode, startDate));
    }

    @Override
    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public boolean courseExists(String courseCode, LocalDate startDate) {
        return courseRepository.existsById(new CourseId(courseCode, startDate));
    }

    @Override
    public List<Lesson> getLessonsByCourse(String courseCode, LocalDate startDate) {
        return lessonRepository.findByCourseCourseCodeAndCourseStartDate(courseCode, startDate);
    }

    @Override
    public Optional<Lesson> getLessonById(Long id) {
        return lessonRepository.findById(id);
    }

    @Override
    public Lesson saveLesson(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    @Override
    public void deleteLesson(Long id) {
        lessonRepository.deleteById(id);
    }
}
