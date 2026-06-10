package lms.service.impl;

import entity.Course;
import entity.Lesson;
import lms.repository.LessonRepository;
import lms.service.LessonService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonServiceImpl extends GenericServiceImpl<Lesson, Long, LessonRepository> implements LessonService {

    @Override
    public List<Lesson> getLessonsByCourse(Course course) {
        return repository.findByCourse(course);
    }
}
