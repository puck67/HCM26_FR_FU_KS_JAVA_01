package com.example.service;

import com.example.entity.Course;
import com.example.entity.Student;
import java.util.List;

public interface StudentService {
    void saveStudent(Student student);
    void updateStudent(Student student);
    void deleteStudent(int id);
    Student getStudent(int id);
    List<Student> getAllStudents();
    void enrollStudentInCourse(int studentId, int courseId);
    void removeStudentFromCourse(int studentId, int courseId);
    List<Course> getCoursesOfStudent(int studentId);
    
    // Advanced queries
    List<Student> findStudentsOlderThan(int age);
    List<Student> findStudentsByName(String name);
    List<Object[]> getStudentsAndTheirCourses();
    List<Student> getStudentsByCourseId(int courseId);
}
