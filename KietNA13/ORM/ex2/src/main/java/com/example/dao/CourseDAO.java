package com.example.dao;

import com.example.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseDAO {

    void save(Course course);

    void update(Course course);

    void delete(int courseId);

    Optional<Course> findById(int id);

    List<Course> findAll();

    List<Course> findByMinCredit(int minCredit);
}
