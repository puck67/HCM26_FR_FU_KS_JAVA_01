package com.example.dao;

import com.example.entity.Student;
import java.util.List;

public interface StudentDAO {
    void save(Student student);
    void update(Student student);
    void delete(int studentId);
    Student findById(int id);
    List<Student> findAll();

    List<Student> findOlderThan(int age);
    List<Student> findWithCourses();
    List<Student> findByName(String name);
    List<Student> findByCourseId(int courseId);
}