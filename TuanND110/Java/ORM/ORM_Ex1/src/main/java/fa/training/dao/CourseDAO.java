package fa.training.dao;

import fa.training.entities.Course;

import java.util.List;

public interface CourseDAO {
	boolean saveCourse(Course course);

	boolean updateCourse(Course course);

	boolean deleteCourse(int courseId);

	List<Course> getAllCourses();
}
