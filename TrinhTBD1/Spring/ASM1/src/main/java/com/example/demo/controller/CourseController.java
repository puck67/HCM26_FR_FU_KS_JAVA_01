package com.example.demo.controller;

import com.example.demo.model.Course;
import com.example.demo.service.CourseService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        logger.info("Serving the create course form page.");
        model.addAttribute("course", new Course());
        return "create_course";
    }

    @PostMapping("/create")
    public String createCourse(@Valid @ModelAttribute("course") Course course, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorLog = new StringBuilder("Validation failed during course creation: ");
            bindingResult.getFieldErrors().forEach(error -> 
                errorLog.append("[Field: ")
                        .append(error.getField())
                        .append(", Rejected Value: ")
                        .append(error.getRejectedValue())
                        .append(", Message: ")
                        .append(error.getDefaultMessage())
                        .append("] ")
            );
            logger.warn(errorLog.toString());
            return "create_course";
        }

        courseService.saveCourse(course);
        logger.info("Successfully created course redirecting to success page.");
        return "redirect:/courses/success";
    }

    @GetMapping("/success")
    public String showSuccessPage() {
        logger.info("Serving the course creation success page.");
        return "create_success";
    }
}
