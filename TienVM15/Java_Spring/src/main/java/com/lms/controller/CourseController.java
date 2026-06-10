package com.lms.controller;

import com.lms.controller.base.GenericController;
import com.lms.entity.Course;
import com.lms.entity.CourseId;
import com.lms.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/courses")
public class CourseController extends GenericController<Course, CourseId> {

    public CourseController(CourseService service) {
        super(service, "courses", "course");
    }

    @Override
    protected Class<Course> getEntityClass() {
        return Course.class;
    }
}
