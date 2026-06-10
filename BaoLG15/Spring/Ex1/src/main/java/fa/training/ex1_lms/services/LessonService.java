package fa.training.ex1_lms.services;

import fa.training.ex1_lms.entities.Course;
import fa.training.ex1_lms.entities.Lesson;

import java.util.List;

public interface LessonService extends GenericService<Lesson, Long> {
    List<Lesson> getLessonsByCourse(Course course);
}
