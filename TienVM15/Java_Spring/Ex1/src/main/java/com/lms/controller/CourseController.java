package com.lms.controller;

import com.lms.controller.base.GenericController;
import com.lms.entity.Course;
import com.lms.entity.CourseId;
import com.lms.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @PostMapping("/save")
    public String saveCourse(@Valid @ModelAttribute("course") Course course,
                             BindingResult result,
                             @RequestParam(value = "isEdit", required = false, defaultValue = "false") boolean isEdit,
                             Model model) {
        if (result.hasErrors()) {
            return renderLayout(model, "form :: body");
        }

        if (!isEdit) {
            CourseId id = new CourseId(course.getCourseCode(), course.getStartDate());
            if (service.findById(id).isPresent()) {
                model.addAttribute("errorMessage", "Course already exists!");
                return renderLayout(model, "form :: body");
            }
            service.save(course);
            model.addAttribute("successMessage", true);
            model.addAttribute("savedCourseCode", course.getCourseCode());
            model.addAttribute("savedStartDate", course.getStartDate().toString());
            return renderLayout(model, "form :: body");
        } else {
            service.save(course);
            return "redirect:/courses";
        }
    }
}
