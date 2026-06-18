package com.lms.service;

import com.lms.model.Course;
import com.lms.model.CourseId;
import com.lms.service.base.GenericService;
import java.util.List;

public interface CourseService extends GenericService<Course, CourseId> {
    List<Course> findByCourseCode(String courseCode);
}
