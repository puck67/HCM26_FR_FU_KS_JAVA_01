package service;

import entity.Course;
import entity.Student;
import java.util.List;
import java.util.Set;

public interface StudentService {
    void createStudent(String name, int age);
    void updateStudent(int id, String name, int age);
    void deleteStudent(int id);
    Student getStudentById(int id);
    List<Student> getAllStudents();
    
    // Enrollment relationships
    void enrollStudentInCourse(int studentId, int courseId);
    void removeStudentFromCourse(int studentId, int courseId);
    Set<Course> getCoursesOfStudent(int studentId);

    // Queries
    List<Student> findStudentsOlderThan(int age);
    List<Object[]> findStudentsWithCourses();
    List<Student> findStudentsByName(String name);
    List<Object[]> countStudentsPerCourse();
    List<Student> findStudentsEnrolledInCourse(int courseId);
}
