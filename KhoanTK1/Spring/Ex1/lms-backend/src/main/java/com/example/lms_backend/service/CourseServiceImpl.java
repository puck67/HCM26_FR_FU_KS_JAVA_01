package com.example.lms_backend.service;

import com.example.lms_backend.entity.Course;
import com.example.lms_backend.entity.CourseId;
import com.example.lms_backend.repository.CourseRepository;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl extends BaseServiceImpl<Course, CourseId> implements CourseService {

    public CourseServiceImpl(CourseRepository repository) {
        super(repository);
    }
}
