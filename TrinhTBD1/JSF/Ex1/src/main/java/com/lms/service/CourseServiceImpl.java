package com.lms.service;

import com.lms.model.Course;
import com.lms.model.CourseId;
import com.lms.repository.CourseRepository;
import com.lms.service.base.GenericServiceImpl;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CourseServiceImpl extends GenericServiceImpl<Course, CourseId, CourseRepository> implements CourseService {
    @Override
    public List<Course> findByCourseCode(String courseCode) {
        return repository.findByCourseCode(courseCode);
    }
}
