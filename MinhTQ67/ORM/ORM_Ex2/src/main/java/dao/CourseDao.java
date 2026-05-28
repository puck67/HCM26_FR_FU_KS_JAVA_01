package dao;

import entity.Course;

import java.util.List;

public interface CourseDao {
    void save(Course course);
    void update(Course course);
    void delete(int courseId);
    Course findById(int id);
    List<Course> findAll();
}
