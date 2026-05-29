package dao;

import entity.Student;
import java.util.List;

public interface StudentDAO {
    void save(Student student);
    void update(Student student);
    void delete(int studentId);
    Student findById(int id);
    List<Student> findAll();
    
    // Task 6: Advanced Queries
    List<Student> findOlderThan(int age);
    List<Object[]> findStudentsWithCourses();
    List<Student> findByName(String name);
    List<Object[]> countStudentsPerCourse();
    List<Student> findStudentsEnrolledInCourse(int courseId);
}
