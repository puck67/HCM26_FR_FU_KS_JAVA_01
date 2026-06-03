package fa.training.dao;

import fa.training.entities.Course;

import java.util.List;

public interface CourseDAO {

    void save(Course course);

    void update(Course course);

    void delete(int id);

    Course getById(int id);

    List<Course> getAll();

    List<Course> findByCreditGreaterThan(int credit);

    List<Object[]> countStudentsPerCourse();
}
