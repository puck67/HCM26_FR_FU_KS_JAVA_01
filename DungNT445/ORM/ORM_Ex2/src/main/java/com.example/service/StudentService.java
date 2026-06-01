package com.example.service;

import com.example.entity.Student;
import java.util.List;

public interface StudentService {
    void createStudent(String name, int age);
    void updateStudent(int id, String name, int age);
    void deleteStudent(int id);
    Student getStudentById(int id);
    List<Student> getAllStudents();
    
    // Relationship management
    void enrollStudentInCourse(int studentId, int courseId);
    void removeStudentFromCourse(int studentId, int courseId);
    List<com.example.entity.Course> getCoursesOfStudent(int studentId);

    // Reports
    List<Student> findOlderThan(int age);
    List<Object[]> findAllStudentsWithCourses();
    List<Student> findByName(String name);
    List<Student> findStudentsEnrolledInCourse(int courseId);
}
