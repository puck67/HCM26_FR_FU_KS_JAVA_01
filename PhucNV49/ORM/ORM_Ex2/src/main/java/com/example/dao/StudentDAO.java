package com.example.dao;

import com.example.entity.Student;
import java.util.List;

public interface StudentDAO {
    void save(Student student);
    void update(Student student);
    void delete(int studentId);
    Student findById(int id);
    List<Student> findAll();
    
    // Task 6: Advanced queries
    List<Student> findByAgeGreaterThan(int age); // HQL
    List<Student> findByNameNamedQuery(String name); // Named Query
    List<Object[]> findAllStudentsWithCourseTitles(); // HQL Join Query
    List<Student> findStudentsByCourseId(int courseId); // Parameterized Query
}
