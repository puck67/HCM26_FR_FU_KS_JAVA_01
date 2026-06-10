package com.example.Ex1.repository;

import com.example.Ex1.model.Course;
import com.example.Ex1.model.CourseId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, CourseId> {
}
