package dao;

import entities.Course;
import entities.Student;

import java.util.List;

public interface CourseDAO {
    void save(Course course);
    Course findById(int id);
    List<Course> findAll();
    void update(Course course);
    void delete(int id);
}
