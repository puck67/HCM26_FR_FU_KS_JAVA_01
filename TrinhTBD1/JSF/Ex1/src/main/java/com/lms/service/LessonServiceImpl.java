package com.lms.service;

import com.lms.model.Course;
import com.lms.model.Lesson;
import com.lms.repository.LessonRepository;
import com.lms.service.base.GenericServiceImpl;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LessonServiceImpl extends GenericServiceImpl<Lesson, Long, LessonRepository> implements LessonService {
    @Override
    public List<Lesson> getLessonsForCourse(Course course) {
        return repository.findByCourse(course);
    }
}
