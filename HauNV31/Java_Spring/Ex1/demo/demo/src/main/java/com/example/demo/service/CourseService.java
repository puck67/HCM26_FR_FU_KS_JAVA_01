package com.example.demo.service;

import com.example.demo.model.Course;
import com.example.demo.model.CourseId;
import com.example.demo.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAllByOrderByIdCourseCodeAsc();
    }

    public Optional<Course> getCourseById(String courseCode, LocalDate startDate) {
        CourseId id = new CourseId(courseCode, startDate);
        return courseRepository.findById(id);
    }

    public void deleteCourse(String courseCode, LocalDate startDate) {
        CourseId id = new CourseId(courseCode, startDate);
        courseRepository.deleteById(id);
    }

    public boolean exists(String courseCode, LocalDate startDate) {
        CourseId id = new CourseId(courseCode, startDate);
        return courseRepository.existsById(id);
    }
}
