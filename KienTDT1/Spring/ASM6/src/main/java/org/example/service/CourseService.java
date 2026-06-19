package org.example.service;

import org.example.entity.Course;
import org.example.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    
    @Autowired
    private CourseRepository courseRepository;
    
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
    
    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElse(null);
    }
    
    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }
    
    public Course updateCourse(Long id, Course course) {
        Course existingCourse = courseRepository.findById(id).orElse(null);
        if (existingCourse != null) {
            existingCourse.setName(course.getName());
            existingCourse.setCode(course.getCode());
            existingCourse.setDescription(course.getDescription());
            existingCourse.setCredits(course.getCredits());
            return courseRepository.save(existingCourse);
        }
        return null;
    }
    
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }
}
