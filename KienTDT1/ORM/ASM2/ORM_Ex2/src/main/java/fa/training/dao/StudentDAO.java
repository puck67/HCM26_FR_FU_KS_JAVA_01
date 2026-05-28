package fa.training.dao;

import fa.training.entity.Student;

import java.util.List;

public interface StudentDAO {
    void save(Student s);
    void update(Student s);
    void delete(int id);
    Student findById(int id);
    List<Student> findAll();
    List<Student> findStudentsOlderThan(int age);

    List<Student> findByName(String name);

    List<Object[]> listStudentsAndCourses();

    List<Student> findStudentsByCourse(int courseId);
}
