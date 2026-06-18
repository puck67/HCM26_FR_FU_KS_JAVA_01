package fa.training.lms.controllers;

import fa.training.lms.controllers.base.GenericController;
import fa.training.lms.model.Course;
import fa.training.lms.services.CourseService;
import fa.training.lms.utils.Validator;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/courses")
public class CourseController extends GenericController<Course, Integer> {

    private final Validator validator;

    public CourseController(CourseService courseService, Validator validator) {
        super(courseService, "courses", "course");
        this.validator = validator;
    }

    @Override
    protected Class<Course> getEntityClass() {
        return Course.class;
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("course") Course course, BindingResult result, Model model) {
        validator.validate(course, result);
        
        if (result.hasErrors()) {
            return viewFolder + "/form";
        }
        
        service.save(course);
        return "redirect:/" + viewFolder;
    }
}
