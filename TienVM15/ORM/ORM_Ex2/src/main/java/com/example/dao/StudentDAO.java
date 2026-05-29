package com.example.dao;

import com.example.entity.Student;

import java.util.List;

/**
 * StudentDAO extends GenericDAO and adds Student-specific query operations.
 */
public interface StudentDAO extends GenericDAO<Student, Integer> {

    /** Paginated query using HQL */
    List<Student> findAllPaginated(int offset, int limit);

    /** HQL query: find students older than given age */
    List<Student> findOlderThan(int age);

    /** HQL Join query: return pairs of [Student, Course] for all enrollments */
    List<Object[]> findStudentsAndCourses();

    /** Named Query: find students whose name contains the given keyword */
    List<Student> findByName(String name);

    /** HQL subquery: find students not enrolled in any course */
    List<Student> findUnenrolledStudents();
}
