package com.example.ex1.repository;

import com.example.ex1.entity.Course;
import com.example.ex1.entity.CourseId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, CourseId> {
}
