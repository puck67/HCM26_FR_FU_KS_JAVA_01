package org.example.lms.controller;

import jakarta.validation.Valid;
import org.example.lms.model.Course;
import org.example.lms.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 *   GET  /courses/new    → render the empty "New Course" form
 *   POST /courses/create → validate and process the submitted form
 */
@Controller
@RequestMapping("/courses")
public class CourseController {


    // GET/courses/new
    @GetMapping("/new")
    public String showNewCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "create_course"; // resolves to templates/create_course.html
    }


    // POST/courses/create
    @PostMapping("/create")
    public String createCourse(@Valid @ModelAttribute("course") Course course,
                               BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            System.out.println("Validation failed! Errors: " + bindingResult.getAllErrors());
            return "create_course";
        }

        System.out.println("New course created successfully: " + course);
        return "redirect:/courses/success";
    }


    // GET /courses/success
    // Renders the success confirmation page after a valid submission.
    @GetMapping("/success")
    public String showSuccessPage() {
        return "create_success"; // resolves to templates/create_success.html
    }
}