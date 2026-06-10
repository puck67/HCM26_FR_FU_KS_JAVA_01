package lms.service.impl;

import entity.Course;
import entity.CourseId;
import lms.repository.CourseRepository;
import lms.service.CourseService;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl extends GenericServiceImpl<Course, CourseId, CourseRepository> implements CourseService {
}
