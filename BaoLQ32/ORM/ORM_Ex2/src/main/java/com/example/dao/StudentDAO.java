package com.example.dao;

import com.example.entity.Student;
import java.util.List;

public interface StudentDAO {
    void save(Student student);
    void update(Student student);
    void delete(int studentId);
    Student findById(int id);
    List<Student> findAll();

    // Advanced Queries
    List<Student> findOlderThan(int age); // HQL
    List<Student> findByName(String name); // Named Query
    List<Object[]> findStudentsWithCourseTitles(); // HQL Join Query
    List<Student> findStudentsByCourse(int courseId); // Parameterized Query
}
