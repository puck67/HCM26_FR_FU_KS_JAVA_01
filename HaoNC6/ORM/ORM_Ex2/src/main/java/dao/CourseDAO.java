package dao;

import entity.Course;
import java.util.List;

public interface CourseDAO {
    void save(Course course);
    void update(Course course);
    void delete(int courseId);
    Course findById(int id);
    List<Course> findAll();
    
    // Task 6: Criteria Query
    List<Course> findByMinCredit(int credit);
}
