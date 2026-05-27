package dao;

import entity.Student;

import java.util.List;

public interface StudentDao {
    void save(Student student);
    void update(Student student);
    void delete(int studentId);
    Student findById(int id);
    List<Student> findAll();
}
