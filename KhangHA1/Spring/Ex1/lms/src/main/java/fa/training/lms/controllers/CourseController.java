package fa.training.lms.controllers;

import fa.training.lms.controllers.base.GenericController;
import fa.training.lms.entities.Course;
import fa.training.lms.entities.CourseId;
import fa.training.lms.repositories.CourseRepository;
import fa.training.lms.repositories.LessonRepository;
import fa.training.lms.services.CourseService;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/courses")
public class CourseController extends GenericController<Course, CourseId> {

    public CourseController(CourseService courseService) {
        super(courseService, "courses", "course");
    }

    @Override
    protected Class<Course> getEntityClass() {
        return Course.class;
    }
}
