package com.example.ex2.service.impl;

import com.example.ex2.entity.Course;
import com.example.ex2.entity.Student;
import com.example.ex2.repository.CourseRepository;
import com.example.ex2.service.CourseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public Course updateCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public void deleteCourse(int id) {
        courseRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Course getCourse(int id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course with ID " + id + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> findCoursesWithCreditGreaterThan(int credit) {
        return courseRepository.findByCreditGreaterThanEqual(credit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> getStudentCountPerCourse() {
        return courseRepository.findCourseStudentCount();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> getStudentsOfCourse(int courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course with ID " + courseId + " not found"));
        return new ArrayList<>(course.getStudents());
    }
}
