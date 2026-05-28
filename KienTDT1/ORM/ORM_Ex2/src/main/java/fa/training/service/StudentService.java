package fa.training.service;

import fa.training.entity.*;
import java.util.*;

public interface StudentService {

    void createStudent(String name, int age);
    void updateStudent(int id, String name, int age);
    void deleteStudent(int id);
    Student getStudentById(int id);
    List<Student> getAllStudents();

    void enrollStudentInCourse(int studentId, int courseId);
    void removeStudentFromCourse(int studentId, int courseId);

    Set<Course> getCoursesOfStudent(int studentId);
}