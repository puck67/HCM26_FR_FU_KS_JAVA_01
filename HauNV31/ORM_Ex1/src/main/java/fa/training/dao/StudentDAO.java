package fa.training.dao;

import fa.training.entities.Student;

import java.util.List;

public interface StudentDAO {

    void save(Student student);

    void update(Student student);

    void delete(int id);

    Student getById(int id);

    List<Student> getAll();

    List<Student> getAll(int page, int pageSize);

    List<Student> findOlderThan(int age);

    List<Student> findByName(String name);

    List<Object[]> findStudentsWithCourses();

    List<Student> findStudentsNotEnrolled();
}
