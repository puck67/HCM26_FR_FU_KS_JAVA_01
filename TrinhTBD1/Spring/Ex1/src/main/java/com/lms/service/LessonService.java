package com.lms.service;

import com.lms.model.Course;
import com.lms.model.Lesson;
import com.lms.service.base.GenericService;
import java.util.List;

public interface LessonService extends GenericService<Lesson, Long> {
    List<Lesson> getLessonsForCourse(Course course);
}
