package lms.service;

import entity.Course;
import entity.Lesson;

import java.util.List;

public interface LessonService extends GenericService<Lesson, Long> {
    List<Lesson> getLessonsByCourse(Course course);
}
