package com.example.service;

import com.example.entity.Course;
import com.example.entity.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {

    void createStudent(String name, int age);

    void updateStudent(int id, String name, int age);

    void deleteStudent(int id);

    Optional<Student> getStudentById(int id);

    List<Student> getAllStudents();

    void enrollStudentInCourse(int studentId, int courseId);

    void removeStudentFromCourse(int studentId, int courseId);

    List<Course> getCoursesOfStudent(int studentId);

    List<Student> findOlderThan(int age);

    List<Student> findByName(String name);

    List<Object[]> findAllWithCourses();

    List<Student> findByCourseId(int courseId);

    List<Object[]> countPerCourse();
}
