package fa.training.dao;

import fa.training.entities.Student;
import fa.training.entities.Course;

import java.util.List;

public interface CourseDAO extends GenericDAO<Course, Integer> {
    List<Student> getStudentsByCourse(int courseId);

    List<Course> findCoursesWithCreditGreaterThan(int credit);

    List<Object[]> countStudentsPerCourse();
}
