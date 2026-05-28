package com.example.dao;

import com.example.entity.Student;
import java.util.List;

public interface StudentDAO extends GenericDAO<Student, Integer> {
    List<Student> findStudentsOlderThan(int age);
    List<Object[]> findStudentsAndCoursesHqlJoin();
    List<Student> findByName(String name);
    List<Student> findStudentsEnrolledInCourse(int courseId);
}
