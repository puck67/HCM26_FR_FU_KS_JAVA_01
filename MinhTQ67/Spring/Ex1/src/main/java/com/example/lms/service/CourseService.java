package com.example.lms.service;

import com.example.lms.entity.Course;
import com.example.lms.entity.CourseId;
import com.example.lms.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    public List<Course> saveAllCourses(List<Course> courses) {
        return courseRepository.saveAll(courses);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Page<Course> getAllCourses(Pageable pageable) {
        return courseRepository.findAll(pageable);
    }

    public Optional<Course> getCourseById(String courseCode, LocalDate startDate) {
        return courseRepository.findById(new CourseId(courseCode, startDate));
    }
}
