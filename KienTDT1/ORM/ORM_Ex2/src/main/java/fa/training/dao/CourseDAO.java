package fa.training.dao;

import fa.training.entity.Course;
import java.util.List;

public interface CourseDAO {
    void save(Course c);
    void update(Course c);
    void delete(int id);
    Course findById(int id);
    List<Course> findAll();
    List<Course> findCoursesWithCreditGreaterThan(int credit);

    List<Object[]> countStudentsInCourses();
}