package com.example.demo.service;

import com.example.demo.model.Course;
import com.example.demo.model.CourseId;
import com.example.demo.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Course save(Course course) {
        return courseRepository.save(course);
    }

    public Course getRequired(CourseId id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));
    }

    public List<Course> findAll() {
        return courseRepository.findAll();
    }
}
