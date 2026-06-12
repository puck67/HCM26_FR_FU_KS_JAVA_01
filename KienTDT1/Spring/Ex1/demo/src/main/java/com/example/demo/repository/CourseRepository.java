package com.example.demo.repository;

import com.example.demo.model.Course;
import com.example.demo.model.CourseId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, CourseId> {
}
