package fa.training.dao;

import fa.training.entities.Course;
import java.util.List;

public interface CourseDAO {
    void save(Course course);
    void update(Course course);
    void delete(int courseId);
    Course findById(int id);
    List<Course> findAll();
    
    // Advanced queries
    List<Course> findCoursesWithCreditGreaterThan(int creditValue);
    List<Object[]> countStudentsPerCourse();
}
