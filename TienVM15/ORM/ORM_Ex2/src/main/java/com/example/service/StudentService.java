package com.example.service;

import com.example.entity.Student;

import java.util.List;

/**
 * StudentService provides business operations around Student management.
 */
public interface StudentService {
    void createStudent(String name, int age);
    boolean updateStudent(int id, String name, int age);
    boolean deleteStudent(int id);
    Student getStudent(int id);
    List<Student> getAllStudents();
    List<Student> getAllStudentsPaginated(int page, int size);
    List<Student> getStudentsOlderThan(int age);
    List<Object[]> getStudentsAndCourses();
    List<Student> getStudentsByName(String name);
    List<Student> getUnenrolledStudents();
}
