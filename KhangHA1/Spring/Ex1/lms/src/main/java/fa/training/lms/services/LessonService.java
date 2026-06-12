package fa.training.lms.services;

import fa.training.lms.entities.Course;
import fa.training.lms.entities.Lesson;
import fa.training.lms.services.base.GenericService;

import java.util.List;

public interface LessonService extends GenericService<Lesson, Integer> {
    List<Lesson> findByCourse(Course course);
}
