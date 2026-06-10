package com.lms.repository;

import com.lms.model.Course;
import com.lms.model.CourseId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, CourseId> {
    List<Course> findByCourseCode(String courseCode);
}
