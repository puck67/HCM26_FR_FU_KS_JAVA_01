package com.example.demo.service.impl;

import com.example.demo.model.Course;
import com.example.demo.repository.CourseRepository;
import com.example.demo.service.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl implements CourseService {

    private static final Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);

    private final CourseRepository courseRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public Course saveCourse(Course course) {
        StringBuilder sb = new StringBuilder();
        sb.append("Saving new course: [Title: ")
          .append(course.getTitle())
          .append(", Instructor: ")
          .append(course.getInstructorName())
          .append(", Email: ")
          .append(course.getInstructorEmail())
          .append("]");
        
        logger.info(sb.toString());
        return courseRepository.save(course);
    }
}
