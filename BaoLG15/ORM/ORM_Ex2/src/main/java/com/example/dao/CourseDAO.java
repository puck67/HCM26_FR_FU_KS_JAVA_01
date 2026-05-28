package com.example.dao;

import com.example.entity.Course;

import java.util.List;

public interface CourseDAO extends GenericDAO<Course, Integer> {
    List<Course> findCoursesWithCreditGreaterThan(int credit);

    List<Object[]> countStudentsPerCourse();

    Course findByIdWithStudents(int id);
}
