package com.example.EX1.repository;

import com.example.EX1.model.Course;
import com.example.EX1.model.CourseId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, CourseId> {
}
