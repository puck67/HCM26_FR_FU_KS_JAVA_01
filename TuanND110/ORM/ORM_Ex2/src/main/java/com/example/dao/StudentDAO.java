package com.example.dao;
import com.example.entity.Student;
import java.util.List;
public interface StudentDAO {
    void save(Student student);
    void update(Student student);
    void delete(int studentId);
    Student findById(int id);
    List<Student> findAll();
    List<Student> findStudentsOlderThan(int age);
    List<Student> findStudentsByName(String name);
    List<Object[]> listStudentsWithCourseTitles();
    List<Student> findStudentsByCourseId(int courseId);
}
