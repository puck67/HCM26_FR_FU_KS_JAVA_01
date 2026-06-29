package com.example.training.repository;

import com.example.training.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository
        extends JpaRepository<Course,Long> {
    java.util.List<Course> findByCourseNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String courseName, String description);
}