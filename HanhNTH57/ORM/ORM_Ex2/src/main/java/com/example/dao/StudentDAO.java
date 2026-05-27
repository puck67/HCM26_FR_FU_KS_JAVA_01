package com.example.dao;

import com.example.entity.Student;
import java.util.List;
import java.util.Map;

public interface StudentDAO {
    void save(Student student);
    void update(Student student);
    void delete(int studentId);
    Student findById(int id);
    List<Student> findAll();
    
    // Advanced Queries
    List<Student> findByAgeGreaterThan(int age);
    List<Student> findByName(String name);
    List<Object[]> findAllStudentsWithCourseTitles();
    List<Student> findByCourseId(int courseId);
    
    // Bonus
    List<Student> findAllPaginated(int pageNumber, int pageSize);
    List<Student> findStudentsNotEnrolled();
}
