package com.lms.service.impl;

import com.lms.entity.Course;
import com.lms.entity.CourseId;
import com.lms.repository.CourseRepository;
import com.lms.service.CourseService;
import com.lms.service.base.GenericServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl extends GenericServiceImpl<Course, CourseId> implements CourseService {
    public CourseServiceImpl(CourseRepository repository) {
        super(repository);
    }
}
