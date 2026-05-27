package com.example.dao;

import com.example.entity.Course;

import java.util.List;
import java.util.Map;

/**
 * CourseDAO extends GenericDAO and adds Course-specific query operations.
 */
public interface CourseDAO extends GenericDAO<Course, Integer> {

    /** Criteria API query: find courses with credit greater than minCredit */
    List<Course> findWithCreditGreaterThan(int minCredit);

    /** HQL Aggregation query: count number of students per course */
    Map<String, Long> getStudentCountPerCourse();
}
