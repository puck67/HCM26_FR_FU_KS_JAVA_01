package com.example.service;

import com.example.entity.Course;
import com.example.entity.Student;

import java.util.List;

public interface StudentService {

    Student createStudent(String name, int age);

    Student updateStudent(int id, String name, int age);

    void deleteStudent(int id);

    Student getStudentById(int id);

    List<Student> getAllStudents();

    void enrollStudentInCourse(int studentId, int courseId);

    void removeStudentFromCourse(int studentId, int courseId);

    List<Course> getCoursesOfStudent(int studentId);

    List<Student> findStudentsOlderThan(int age);

    List<Student> findStudentsByName(String name);

    List<Object[]> getStudentsWithCourses();

    List<Student> findStudentsByCourseId(int courseId);

    List<Object[]> countStudentsPerCourse();
}
