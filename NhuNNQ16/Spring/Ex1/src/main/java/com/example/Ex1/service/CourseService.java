package com.example.Ex1.service;

import com.example.Ex1.model.Course;
import com.example.Ex1.model.CourseId;
import com.example.Ex1.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(String courseCode, LocalDate startDate) {
        return courseRepository.findById(new CourseId(courseCode, startDate));
    }

    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    public boolean exists(String courseCode, LocalDate startDate) {
        return courseRepository.existsById(new CourseId(courseCode, startDate));
    }
}

