package fa.training.lms.interfaces;

import fa.training.lms.entities.Lesson;
import fa.training.lms.interfaces.base.GenericService;

import fa.training.lms.entities.CourseId;

public interface LessonService extends GenericService<Lesson, Long> {
    java.util.List<Lesson> getLessonsByCourseId(CourseId courseId);
}
