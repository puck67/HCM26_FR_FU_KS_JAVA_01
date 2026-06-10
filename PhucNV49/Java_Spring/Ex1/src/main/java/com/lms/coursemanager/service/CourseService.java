package com.lms.coursemanager.service;

import com.lms.coursemanager.dto.CourseDTO;

import java.time.LocalDate;
import java.util.List;

public interface CourseService {
    CourseDTO createCourse(CourseDTO courseDTO);
    CourseDTO getCourse(String courseCode, LocalDate startDate);
    List<CourseDTO> getAllCourses();
    boolean exists(String courseCode, LocalDate startDate);
}
