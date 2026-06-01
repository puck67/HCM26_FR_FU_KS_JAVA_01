package com.example.service;
import com.example.entity.Course;
import com.example.entity.Student;
import java.util.List;
public interface StudentService {
    Student createStudent(String name, int age);
    Student updateStudent(int id, String name, int age);
    boolean deleteStudent(int id);
    Student getStudentById(int id);
    List<Student> getAllStudents();
    boolean enrollStudentInCourse(int studentId, int courseId);
    boolean removeStudentFromCourse(int studentId, int courseId);
    List<Course> getCoursesOfStudent(int studentId);
    List<Student> findStudentsOlderThan(int age);
    List<Student> findStudentsByName(String name);
    List<Object[]> listStudentsWithCourses();
    List<Student> findStudentsEnrolledInCourse(int courseId);
}
