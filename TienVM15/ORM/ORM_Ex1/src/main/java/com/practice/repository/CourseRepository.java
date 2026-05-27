package com.practice.repository;

import com.practice.entity.Course;
import java.util.List;
import java.util.Map;

public interface CourseRepository extends GenericRepository<Course, Integer> {
    List<Course> findWithCreditGreaterThan(int minCredit); // Using Criteria API
    Map<String, Long> getStudentCountPerCourse();          // Using Aggregation Query
}

