package com.example.service;

import com.example.entity.Course;
import com.example.entity.Student;
import java.util.List;
import java.util.Set;

public interface StudentService {
    Student createStudent(String name, int age);
    void updateStudent(int id, String name, int age);
    void deleteStudent(int id);
    Student getStudentById(int id);
    List<Student> getAllStudents();
    void enrollStudentInCourse(int studentId, int courseId);
    void removeStudentFromCourse(int studentId, int courseId);
    Set<Course> getCoursesOfStudent(int studentId);
    List<Student> findStudentsOlderThan(int age);
    List<Student> findStudentsByName(String name);
    List<Student> getStudentsWithCourses();
    List<Student> getStudentsEnrolledInCourse(int courseId);
}
