package com.lms.service;

import com.lms.model.Course;
import com.lms.model.CourseId;
import com.lms.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    public Optional<Course> findById(String courseCode, LocalDate startDate) {
        return courseRepository.findById(new CourseId(courseCode, startDate));
    }

    public boolean save(Course course) {
        CourseId id = new CourseId(course.getCourseCode(), course.getStartDate());
        if (courseRepository.existsById(id)) {
            return false; // duplicate
        }
        courseRepository.save(course);
        return true;
    }

    public void delete(String courseCode, LocalDate startDate) {
        courseRepository.deleteById(new CourseId(courseCode, startDate));
    }
}
