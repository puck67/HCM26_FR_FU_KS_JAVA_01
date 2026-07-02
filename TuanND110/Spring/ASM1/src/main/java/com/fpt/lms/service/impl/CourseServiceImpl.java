package com.fpt.lms.service.impl;

import com.fpt.lms.model.Course;
import com.fpt.lms.repository.CourseRepository;
import com.fpt.lms.service.CourseService;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl extends GenericServiceImpl<Course, Long, CourseRepository> implements CourseService {

    public CourseServiceImpl(CourseRepository repository) {
        super(repository);
    }
}
