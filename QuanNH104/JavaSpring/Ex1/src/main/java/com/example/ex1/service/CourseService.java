package com.example.ex1.service;

import com.example.ex1.entity.Course;
import com.example.ex1.entity.CourseId;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    List<Course> findAll();
    Optional<Course> findById(CourseId id);
    boolean existsById(CourseId id);
    Course save(Course course);
}
