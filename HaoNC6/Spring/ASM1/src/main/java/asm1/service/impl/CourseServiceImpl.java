package asm1.service.impl;

import asm1.entity.Course;
import org.springframework.stereotype.Service;
import asm1.repository.CourseRepository;
import asm1.service.CourseService;

@Service
public class CourseServiceImpl extends GenericServiceImpl<Course, Long, CourseRepository> implements CourseService {
}
