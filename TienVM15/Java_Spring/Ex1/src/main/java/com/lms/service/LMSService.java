package com.lms.service;

import com.lms.entity.Course;
import com.lms.entity.Lesson;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LMSService {
    List<Course> getAllCourses();
    Optional<Course> getCourseById(String courseCode, LocalDate startDate);
    Course saveCourse(Course course);
    boolean courseExists(String courseCode, LocalDate startDate);

    List<Lesson> getLessonsByCourse(String courseCode, LocalDate startDate);
    Optional<Lesson> getLessonById(Long id);
    Lesson saveLesson(Lesson lesson);
    void deleteLesson(Long id);
}
