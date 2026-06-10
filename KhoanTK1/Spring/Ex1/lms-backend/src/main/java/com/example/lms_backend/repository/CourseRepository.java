package com.example.lms_backend.repository;

import com.example.lms_backend.entity.Course;
import com.example.lms_backend.entity.CourseId;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends BaseRepository<Course, CourseId> {
}
