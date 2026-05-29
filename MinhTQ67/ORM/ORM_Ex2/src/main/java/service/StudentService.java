package service;

import entity.Course;
import entity.Student;

import java.util.List;

public interface StudentService {
    void createStudent(String name, int age);
    void updateStudent(int id, String name, int age);
    void deleteStudent(int id);
    Student getStudentById(int id);
    List<Student> getAllStudents();
    void enrollStudentInCourse(int studentId, int courseId);
    void removeStudentFromCourse(int studentId, int courseId);
    List<Course> getCoursesOfStudent(int studentId);

    // Advanced queries
    List<Student> findStudentsOlderThan(int age);
    List<Object[]> findStudentsWithCourses();
    List<Student> findStudentsByName(String name);
    List<Student> findStudentsEnrolledInCourse(int courseId);
}

