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
    List<Student> findOlderThan(int age); // HQL Query
    List<Student> findByName(String name); // Named Query
    List<Object[]> findAllWithCourses(); // HQL Join Query: returns array of [Student, courseTitle] or [studentName, courseTitle]
    List<Student> findEnrolledInCourse(int courseId); // Parameterized Query
}
