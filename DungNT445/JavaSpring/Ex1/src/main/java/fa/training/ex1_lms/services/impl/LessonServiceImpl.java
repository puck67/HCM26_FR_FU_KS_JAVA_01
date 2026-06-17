package fa.training.ex1_lms.services.impl;

import fa.training.ex1_lms.entities.Course;
import fa.training.ex1_lms.entities.Lesson;
import fa.training.ex1_lms.repositories.LessonRepository;
import fa.training.ex1_lms.services.LessonService;
import fa.training.ex1_lms.services.base.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LessonServiceImpl extends GenericServiceImpl<Lesson, Long, LessonRepository> implements LessonService {

    @Autowired
    public LessonServiceImpl(LessonRepository lessonRepository) {
        super(lessonRepository);
    }

    @Override
    public List<Lesson> getLessonsByCourse(Course course) {
        return repository.findByCourse(course);
    }
}
