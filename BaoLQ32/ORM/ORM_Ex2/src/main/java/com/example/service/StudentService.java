package com.example.service;

import com.example.entity.Course;
import com.example.entity.Student;
import java.util.List;
import java.util.Set;

public interface StudentService {
    Student createStudent(String name, int age);
    Student updateStudent(int id, String name, int age);
    void deleteStudent(int id);
    Student getStudentById(int id);
    List<Student> getAllStudents();

    // Enrollment operations
    void enrollStudentInCourse(int studentId, int courseId);
    void removeStudentFromCourse(int studentId, int courseId);
    Set<Course> getCoursesOfStudent(int studentId);

    // Advanced Queries
    List<Student> getStudentsOlderThan(int age);
    List<Student> getStudentsByName(String name);
    List<Object[]> getStudentsWithCourseTitles();
    List<Student> getStudentsEnrolledInCourse(int courseId);
}
