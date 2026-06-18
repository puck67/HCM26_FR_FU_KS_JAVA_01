package fa.training.exercise1.services.impl;

import fa.training.exercise1.entities.LmsCourse;
import fa.training.exercise1.entities.LmsLesson;
import fa.training.exercise1.repositories.LmsLessonRepository;
import fa.training.exercise1.services.LmsLessonService;
import fa.training.exercise1.services.base.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LmsLessonServiceImpl extends GenericServiceImpl<LmsLesson, Long, LmsLessonRepository> implements LmsLessonService {

    @Autowired
    public LmsLessonServiceImpl(LmsLessonRepository lessonRepository) {
        super(lessonRepository);
    }

    @Override
    public List<LmsLesson> getLessonsByCourse(LmsCourse course) {
        return repository.findByLmsCourse(course);
    }
}
