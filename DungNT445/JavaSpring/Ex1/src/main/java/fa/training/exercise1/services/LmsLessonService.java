package fa.training.exercise1.services;

import fa.training.exercise1.entities.LmsCourse;
import fa.training.exercise1.entities.LmsLesson;
import fa.training.exercise1.services.base.GenericService;

import java.util.List;

public interface LmsLessonService extends GenericService<LmsLesson, Long> {
    List<LmsLesson> getLessonsByCourse(LmsCourse course);
}
