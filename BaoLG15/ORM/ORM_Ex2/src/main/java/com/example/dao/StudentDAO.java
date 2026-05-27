package com.example.dao;

import com.example.entity.Student;

import java.util.List;

public interface StudentDAO extends GenericDAO<Student, Integer> {
    List<Student> findOlderThan(int age);

    List<Object[]> findStudentsWithCourseTitles();

    List<Student> findByName(String name);

    List<Student> findStudentsByCourseId(int courseId);

    Student findByIdWithCourses(int id);
}
