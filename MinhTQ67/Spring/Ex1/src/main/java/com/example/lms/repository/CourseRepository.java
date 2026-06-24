package com.example.lms.repository;

import com.example.lms.entity.Course;
import com.example.lms.entity.CourseId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, CourseId> {
    List<Course> findByIdCourseCodeOrderByIdStartDateDesc(String courseCode);
}
