package com.example.ex2.service;

import com.example.ex2.entity.Course;
import com.example.ex2.entity.Student;

import java.util.List;

public interface StudentService {

    List<Student> getAllStudents();

    Student saveStudent(Student student);

    Student updateStudent(Student student);

    void deleteStudent(int id);

    Student getStudent(int id);

    List<Student> findStudentsOlderThan(int age);

    List<Student> findStudentsByName(String name);

    void enrollStudentInCourse(int studentId, int courseId);

    void unenrollStudentFromCourse(int studentId, int courseId);

    List<Course> getCoursesOfStudent(int studentId);
}
