package com.lms.service;

import com.lms.entity.Course;
import com.lms.entity.CourseId;
import com.lms.repository.CourseRepository;
import com.lms.service.base.GenericServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class CourseService extends GenericServiceImpl<Course, CourseId> {
    public CourseService(CourseRepository repository) {
        super(repository);
    }
}
