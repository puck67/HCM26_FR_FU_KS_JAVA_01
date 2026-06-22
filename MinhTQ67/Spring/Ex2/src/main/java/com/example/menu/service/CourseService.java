package com.example.menu.service;

import com.example.menu.entity.Course;
import com.example.menu.entity.CourseId;
import com.example.menu.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;

    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(String courseCode, LocalDate startDate) {
        return courseRepository.findById(new CourseId(courseCode, startDate));
    }
}
