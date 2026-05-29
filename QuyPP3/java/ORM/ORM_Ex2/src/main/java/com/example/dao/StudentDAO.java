package com.example.dao;

import com.example.entity.Student;
import java.util.List;

public interface StudentDAO {
    void save(Student student);
    void update(Student student);
    void delete(int studentId);
    Student findById(int id);
    List<Student> findAll();
    
    // Nâng cao
    List<Student> findOlderThan(int age);
    List<Object[]> findAllStudentsWithCourseTitles();
    List<Student> findByNameNamedQuery(String name);
    List<Student> findStudentsByCourseId(int courseId);
}
