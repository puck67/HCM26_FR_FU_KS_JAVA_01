package com.practice.repository;

import com.practice.entity.Student;
import java.util.List;

public interface StudentRepository extends GenericRepository<Student, Integer> {
    List<Student> findAllPaginated(int offset, int limit);
    List<Student> findOlderThan(int age);
    List<Object[]> findStudentsAndCourses();
    List<Student> findByName(String name);
    List<Student> findUnenrolledStudents();
}

