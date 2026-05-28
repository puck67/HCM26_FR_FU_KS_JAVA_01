package fa.training.service;

import fa.training.entity.Course;
import fa.training.entity.Student;

import java.util.List;
import java.util.Set;

public interface StudentService {
    void createStudent(String name, int age);
    void updateStudent(int id, String name, int age);
    void deleteStudent(int id);
    Student getStudentById(int id);
    List<Student> getAllStudents();
    
    void enrollStudentInCourse(int studentId, int courseId);
    void removeStudentFromCourse(int studentId, int courseId);
    Set<Course> getCoursesOfStudent(int studentId);
    
    // Pass-through for advanced queries
    List<Student> getStudentsOlderThan(int age);
    List<Object[]> getStudentsWithCourseTitles();
    List<Student> getStudentsByName(String name);
    List<Student> getStudentsEnrolledInCourse(int courseId);
}
