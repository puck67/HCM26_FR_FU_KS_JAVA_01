package com.example.ex1.service.impl;

import com.example.ex1.entity.Course;
import com.example.ex1.entity.CourseId;
import com.example.ex1.repository.CourseRepository;
import com.example.ex1.service.CourseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    // Constructor Injection thủ công
    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    @Override
    public Optional<Course> findById(CourseId id) {
        return courseRepository.findById(id);
    }

    @Override
    public boolean existsById(CourseId id) {
        return courseRepository.existsById(id);
    }

    @Override
    public Course save(Course course) {
        return courseRepository.save(course);
    }
}
