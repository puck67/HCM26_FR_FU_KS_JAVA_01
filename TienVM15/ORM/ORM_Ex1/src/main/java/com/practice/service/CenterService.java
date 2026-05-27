package com.practice.service;

import com.practice.entity.Course;
import com.practice.entity.Student;

import java.util.List;
import java.util.Map;

public interface CenterService {
    // Student CRUD
    void createStudent(String name, int age);
    boolean updateStudent(int id, String name, int age);
    boolean deleteStudent(int id);
    Student getStudent(int id);
    List<Student> getAllStudents();
    List<Student> getAllStudentsPaginated(int page, int size);

    // Course CRUD
    void createCourse(String title, int credit);
    boolean updateCourse(int id, String title, int credit);
    boolean deleteCourse(int id);
    Course getCourse(int id);
    List<Course> getAllCourses();

    // Enrollment Logic
    void enrollStudent(int studentId, int courseId);
    void removeStudentFromCourse(int studentId, int courseId);
    List<Course> getCoursesOfStudent(int studentId);
    List<Student> getStudentsOfCourse(int courseId);

    // Queries
    List<Student> getStudentsOlderThan(int age);
    List<Object[]> getStudentsAndCourses();
    List<Student> getStudentsByName(String name);
    List<Course> getCoursesWithCreditGreaterThan(int credit);
    Map<String, Long> getStudentCountPerCourse();
    List<Student> getUnenrolledStudents();
}
