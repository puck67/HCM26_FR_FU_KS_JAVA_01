package com.example.dao;

import com.example.entity.Student;

import java.util.List;
import java.util.Optional;

public interface StudentDAO {

    void save(Student student);

    void update(Student student);

    void delete(int studentId);

    Optional<Student> findById(int id);

    List<Student> findAll();

    List<Student> findOlderThan(int age);

    List<Student> findByName(String name);

    List<Object[]> findAllWithCourses();

    List<Student> findByCourseId(int courseId);

    List<Object[]> countPerCourse();
}
