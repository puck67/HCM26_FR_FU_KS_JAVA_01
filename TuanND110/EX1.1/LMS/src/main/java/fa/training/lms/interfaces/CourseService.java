package fa.training.lms.interfaces;

import fa.training.lms.entities.Course;
import fa.training.lms.entities.CourseId;
import fa.training.lms.interfaces.base.GenericService;

public interface CourseService extends GenericService<Course, CourseId> {
    java.util.List<Course> getCoursesByCategory(String category);
}
