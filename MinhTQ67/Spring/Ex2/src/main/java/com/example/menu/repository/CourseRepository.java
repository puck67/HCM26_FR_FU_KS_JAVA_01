package com.example.menu.repository;

import com.example.menu.entity.Course;
import com.example.menu.entity.CourseId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, CourseId> {
    List<Course> findByIdCourseCodeOrderByIdStartDateDesc(String courseCode);
}
