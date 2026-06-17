package com.lms.service;

import com.lms.model.Course;
import com.lms.model.CourseId;
import com.lms.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;

    @Transactional(readOnly = true)
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Course> getCourseById(CourseId id) {
        return courseRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public boolean existsById(CourseId id) {
        return courseRepository.existsById(id);
    }

    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    public void deleteCourse(CourseId id) {
        courseRepository.deleteById(id);
    }
}
